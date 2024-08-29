package com.github.freedownloadhere.pitplayer.simulation

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.google.common.base.Predicates
import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.util.EntitySelectors
import net.minecraft.util.MathHelper
import kotlin.math.abs
import kotlin.math.max

class SimulatedPlayer(world: WorldClient, gameProfile: GameProfile) : EntityOtherPlayerMP(world, gameProfile) {
    private var jumpTicks = 0

    private fun resetSimulate() {
        moveForward = 0.0f
        moveStrafing = 0.0f
        isJumping = false
        isSprinting = false
        isSneaking = false
    }

    fun simulate(sm : SimulatedMovement) {
        resetSimulate()

        moveForward = sm.forward
        moveStrafing = sm.left
        isJumping = sm.jump
        isSprinting = true

        Debug.Logger.regular("Simulating: \u00A73$sm")

        fullSimulation()
    }

    private fun fullSimulation() {
        inWater = worldObj.handleMaterialAcceleration(
                entityBoundingBox.expand(0.0, -0.4000000059604645, 0.0).contract(0.001, 0.001, 0.001),
                net.minecraft.block.material.Material.water,
                this)

        flyToggleTimer = max(0, flyToggleTimer - 1)

        onLivingUpdate2()

        val iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        iattributeinstance.baseValue = capabilities.walkSpeed.toDouble()
        jumpMovementFactor = speedInAir
        if (isSprinting)
            jumpMovementFactor = (jumpMovementFactor.toDouble() + speedInAir.toDouble() * 0.3).toFloat()
        aiMoveSpeed = iattributeinstance.attributeValue.toFloat()

        val axisalignedbb = entityBoundingBox.expand(1.0, 0.5, 1.0)
        val list = worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb)
        for (entity in list)
            if(!entity.isDead)
                entity.onCollideWithPlayer(this)
    }

    private fun onLivingUpdate2() {
        jumpTicks = max(0, jumpTicks - 1)

        if (newPosRotationIncrements > 0) {
            val d0 = posX + (newPosX - posX) / newPosRotationIncrements.toDouble()
            val d1 = posY + (newPosY - posY) / newPosRotationIncrements.toDouble()
            val d2 = posZ + (newPosZ - posZ) / newPosRotationIncrements.toDouble()
            val d3 = MathHelper.wrapAngleTo180_double(newRotationYaw - rotationYaw.toDouble())
            rotationYaw = (rotationYaw.toDouble() + d3 / newPosRotationIncrements.toDouble()).toFloat()
            rotationPitch =
                (rotationPitch.toDouble() + (newRotationPitch - rotationPitch.toDouble()) / newPosRotationIncrements.toDouble()).toFloat()
            --newPosRotationIncrements
            setPosition(d0, d1, d2)
            setRotation(rotationYaw, rotationPitch)
        } else {
            motionX *= 0.98
            motionY *= 0.98
            motionZ *= 0.98
        }

        if (abs(motionX) < 0.005) motionX = 0.0
        if (abs(motionY) < 0.005) motionY = 0.0
        if (abs(motionZ) < 0.005) motionZ = 0.0

        if (isMovementBlocked) {
            isJumping = false
            moveStrafing = 0.0f
            moveForward = 0.0f
            randomYawVelocity = 0.0f
        } else {
            updateEntityActionState()
        }

        if (isJumping) {
            if (isInWater) {
                updateAITick()
            } else if (isInLava) {
                handleJumpLava()
            } else if (onGround && jumpTicks == 0) {
                jump()
                jumpTicks = 10
            }
        } else {
            jumpTicks = 0
        }

        moveStrafing *= 0.98f
        moveForward *= 0.98f
        randomYawVelocity *= 0.9f
        moveEntityWithHeading2(moveStrafing, moveForward)

        val list = worldObj.getEntitiesInAABBexcluding(
            this,
            this.entityBoundingBox.expand(0.20000000298023224, 0.0, 0.20000000298023224),
            Predicates.and(EntitySelectors.NOT_SPECTATING) { pApply1 -> pApply1!!.canBePushed() })
        for (entity in list)
            this.collideWithEntity(entity)
    }

    private fun moveEntityWithHeading2(strafe: Float, forward: Float) {
        if (capabilities.isFlying && this.ridingEntity == null) {
            val d3 = this.motionY
            val f = this.jumpMovementFactor
            this.jumpMovementFactor = capabilities.flySpeed * (if (this.isSprinting) 2 else 1).toFloat()
            moveEntityWithHeading3(strafe, forward)
            this.motionY = d3 * 0.6
            this.jumpMovementFactor = f
        } else {
            moveEntityWithHeading3(strafe, forward)
        }
    }

    private fun moveEntityWithHeading3(strafe : Float, forward : Float) {
        val d0: Double
        var f3: Float
        var f5: Float
        var f6: Float
        if (!this.isInWater || (this as EntityPlayer).capabilities.isFlying) {
            if (this.isInLava && (!(this as EntityPlayer).capabilities.isFlying)) {
                d0 = this.posY
                this.moveFlying(strafe, forward, 0.02f)
                this.moveEntity(this.motionX, this.motionY, this.motionZ)
                this.motionX *= 0.5
                this.motionY *= 0.5
                this.motionZ *= 0.5
                this.motionY -= 0.02
                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(
                        this.motionX,
                        this.motionY + 0.6000000238418579 - this.posY + d0,
                        this.motionZ
                    )
                ) {
                    this.motionY = 0.30000001192092896
                }
            } else {
                var f4 = 0.91f
                if (this.onGround) {
                    f4 = worldObj.getBlockState(
                        BlockPos(
                            MathHelper.floor_double(this.posX),
                            MathHelper.floor_double(this.entityBoundingBox.minY) - 1,
                            MathHelper.floor_double(
                                this.posZ
                            )
                        )
                    ).block.slipperiness * 0.91f
                }

                val f = 0.16277136f / (f4 * f4 * f4)
                f5 = if (this.onGround) {
                    this.aiMoveSpeed * f
                } else {
                    jumpMovementFactor
                }

                this.moveFlying(strafe, forward, f5)
                f4 = 0.91f
                if (this.onGround) {
                    f4 = worldObj.getBlockState(
                        BlockPos(
                            MathHelper.floor_double(this.posX),
                            MathHelper.floor_double(this.entityBoundingBox.minY) - 1,
                            MathHelper.floor_double(
                                this.posZ
                            )
                        )
                    ).block.slipperiness * 0.91f
                }

                if (this.isOnLadder) {
                    f6 = 0.15f
                    this.motionX = MathHelper.clamp_double(this.motionX, (-f6).toDouble(), f6.toDouble())
                    this.motionZ = MathHelper.clamp_double(this.motionZ, (-f6).toDouble(), f6.toDouble())
                    this.fallDistance = 0.0f
                    if (this.motionY < -0.15) {
                        this.motionY = -0.15
                    }

                    val flag = this.isSneaking
                    if (flag && this.motionY < 0.0) {
                        this.motionY = 0.0
                    }
                }

                this.moveEntity(this.motionX, this.motionY, this.motionZ)
                if (this.isCollidedHorizontally && this.isOnLadder) {
                    this.motionY = 0.2
                }

                if ((!worldObj.isBlockLoaded(
                        BlockPos(
                            posX.toInt(), 0, posZ.toInt()
                        )
                    ) || !worldObj.getChunkFromBlockCoords(
                        BlockPos(
                            posX.toInt(), 0, posZ.toInt()
                        )
                    ).isLoaded)
                ) {
                    if (this.posY > 0.0) {
                        this.motionY = -0.1
                    } else {
                        this.motionY = 0.0
                    }
                } else {
                    this.motionY -= 0.08
                }

                this.motionY *= 0.9800000190734863
                this.motionX *= f4.toDouble()
                this.motionZ *= f4.toDouble()
            }
        } else {
            d0 = this.posY
            f5 = 0.8f
            f6 = 0.02f
            f3 = EnchantmentHelper.getDepthStriderModifier(this).toFloat()
            if (f3 > 3.0f) {
                f3 = 3.0f
            }

            if (!this.onGround) {
                f3 *= 0.5f
            }

            if (f3 > 0.0f) {
                f5 += (0.54600006f - f5) * f3 / 3.0f
                f6 += (this.aiMoveSpeed * 1.0f - f6) * f3 / 3.0f
            }

            this.moveFlying(strafe, forward, f6)
            this.moveEntity(this.motionX, this.motionY, this.motionZ)
            this.motionX *= f5.toDouble()
            this.motionY *= 0.800000011920929
            this.motionZ *= f5.toDouble()
            this.motionY -= 0.02
            if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(
                    this.motionX,
                    this.motionY + 0.6000000238418579 - this.posY + d0,
                    this.motionZ
                )
            ) {
                this.motionY = 0.30000001192092896
            }
        }
    }
}