package com.github.freedownloadhere.pitplayer.simulation

import com.github.freedownloadhere.pitplayer.extensions.*
import com.mojang.authlib.GameProfile
import net.minecraft.block.material.Material
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.potion.Potion
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SimulatedPlayer : EntityOtherPlayerMP(world, GameProfile(UUID.randomUUID(), "Bot")) {
    private var jumpTicks = 0

    init {
        noClip = false
        isSneaking = false
        capabilities.isCreativeMode = false
        capabilities.allowFlying = false
        capabilities.isFlying = false
    }

    override fun onUpdate() {
    }

    fun loadState(image : PlayerImage) {
        resetSimulate()
        setPosition(image.pos.x, image.pos.y, image.pos.z)
        setRotation(image.rotationYaw, image.rotationPitch)
        motionX = image.motionX
        motionY = image.motionY
        motionZ = image.motionZ
        jumpTicks = image.jumpTicks
        jumpMovementFactor = image.jumpMovementFactor
        capabilities.setPlayerWalkSpeed(image.walkSpeed)
    }

    private fun resetSimulate() {
        moveForward = 0.0f
        moveStrafing = 0.0f
        isJumping = false
        isSprinting = false
        isSneaking = false
    }

    fun simulate(sm: SimulatedMovement) {
        moveForward = sm.forward
        moveStrafing = sm.left
        isJumping = sm.jump
        isSprinting = true

        fullSimulation()
    }

    private fun fullSimulation() {
        inWater = worldObj.handleMaterialAcceleration(
            entityBoundingBox.expand(0.0, -0.4, 0.0).contract(0.001, 0.001, 0.001),
            Material.water,
            this
        )

        flyToggleTimer = max(0, flyToggleTimer - 1)

        simulateLivingUpdate()

        val iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        iattributeinstance.baseValue = capabilities.walkSpeed.toDouble()
        jumpMovementFactor = speedInAir
        if (isSprinting)
            jumpMovementFactor += speedInAir * 0.3f
        aiMoveSpeed = iattributeinstance.attributeValue.toFloat()

        //val axisalignedbb = entityBoundingBox.expand(1.0, 0.5, 1.0)
        //val list = worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb)
        //for (entity in list)
        //    if (!entity.isDead)
        //        entity.onCollideWithPlayer(this)
    }

    private fun simulateLivingUpdate() {
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

        if (!isJumping) jumpTicks = 0
        else if (isInWater || isInLava) motionY += 0.04
        else if (onGround && jumpTicks == 0) {
            motionY = jumpUpwardsMotion.toDouble()
            if (isPotionActive(Potion.jump))
                motionY += ((getActivePotionEffect(Potion.jump).amplifier + 1).toFloat() * 0.1f).toDouble()
            if (isSprinting) {
                val f = rotationYaw * 0.017453292f
                motionX -= (MathHelper.sin(f) * 0.2f).toDouble()
                motionZ += (MathHelper.cos(f) * 0.2f).toDouble()
            }
            isAirBorne = true
            jumpTicks = 10
        }

        moveStrafing *= 0.98f
        moveForward *= 0.98f
        randomYawVelocity *= 0.9f
        if (!capabilities.isFlying) {
            simulateMoveWithHeading(moveStrafing, moveForward)
        } else {
            val copyMotionY = motionY
            val copyJumpMovementFactor = jumpMovementFactor

            jumpMovementFactor = capabilities.flySpeed * (if (isSprinting) 2 else 1).toFloat()
            simulateMoveWithHeading(moveStrafing, moveForward)

            motionY = copyMotionY * 0.6
            jumpMovementFactor = copyJumpMovementFactor
        }

        // val list = worldObj.getEntitiesInAABBexcluding(
            // this,
            // this.entityBoundingBox.expand(0.2, 0.0, 0.2),
            // Predicates.and(EntitySelectors.NOT_SPECTATING) { pApply1 -> pApply1!!.canBePushed() })
        // for (entity in list)
            // collideWithEntity(entity)
    }

    private fun simulateMoveWithHeading(strafe: Float, forward: Float) {
        if(isInWater && !capabilities.isFlying) {
            simulateWaterMoveWithHeading(strafe, forward)
            return
        }

        if(isInLava && !capabilities.isFlying) {
            simulateLavaMoveWithHeading(strafe, forward)
            return
        }

        var f4 = 0.91f
        if (onGround) {
            f4 *= worldObj.getBlockState(
                BlockPos(
                    MathHelper.floor_double(posX),
                    MathHelper.floor_double(entityBoundingBox.minY) - 1,
                    MathHelper.floor_double(
                        posZ
                    )
                )
            ).block.slipperiness
        }

        val f = 0.16277136f / (f4 * f4 * f4)
        val f5 = if (onGround) {
            aiMoveSpeed * f
        } else {
            jumpMovementFactor
        }

        simulateMoveFlying(strafe, forward, f5)

        f4 = 0.91f
        if (onGround) {
            f4 = worldObj.getBlockState(
                BlockPos(
                    MathHelper.floor_double(posX),
                    MathHelper.floor_double(entityBoundingBox.minY) - 1,
                    MathHelper.floor_double(
                        posZ
                    )
                )
            ).block.slipperiness * 0.91f
        }

        if (isOnLadder) {
            val f6 = 0.15f
            motionX = MathHelper.clamp_double(motionX, (-f6).toDouble(), f6.toDouble())
            motionZ = MathHelper.clamp_double(motionZ, (-f6).toDouble(), f6.toDouble())
            fallDistance = 0.0f
            if (motionY < -0.15) {
                motionY = -0.15
            }

            val flag = isSneaking
            if (flag && motionY < 0.0) {
                motionY = 0.0
            }
        }

        moveEntity(motionX, motionY, motionZ)
        if (isCollidedHorizontally && isOnLadder) {
            motionY = 0.2
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
            motionY = if (posY > 0.0) -0.1 else 0.0
        } else {
            motionY -= 0.08
        }

        motionY *= 0.98
        motionX *= f4.toDouble()
        motionZ *= f4.toDouble()
    }

    private fun simulateWaterMoveWithHeading(strafe: Float, forward : Float) {
        val d0 = posY
        var f5 = 0.8f
        var f6 = 0.02f
        var f3 = EnchantmentHelper.getDepthStriderModifier(this).toFloat()
        f3 = min(3.0f, f3)
        if (!onGround)
            f3 *= 0.5f

        if (f3 > 0.0f) {
            f5 += (0.54600006f - f5) * f3 / 3.0f
            f6 += (aiMoveSpeed * 1.0f - f6) * f3 / 3.0f
        }

        simulateMoveFlying(strafe, forward, f6)
        moveEntity(motionX, motionY, motionZ)

        motionX *= f5.toDouble()
        motionY *= 0.8
        motionZ *= f5.toDouble()
        motionY -= 0.02
        if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6 - posY + d0, motionZ))
            motionY = 0.3

        return
    }

    private fun simulateLavaMoveWithHeading(strafe : Float, forward : Float) {
        val d0 = posY

        simulateMoveFlying(strafe, forward, 0.02f)
        moveEntity(motionX, motionY, motionZ)

        motionX *= 0.5
        motionY *= 0.5
        motionZ *= 0.5
        motionY -= 0.02
        if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6 - posY + d0, motionZ))
            motionY = 0.3

        return
    }

    private fun simulateMoveFlying(strafe : Float, forward : Float, friction : Float) {
        var dist = strafe * strafe + forward * forward
        if(dist < 1.0E-4f) return

        dist = friction / max(1.0f, MathHelper.sqrt_float(dist))

        val maybeSinDist = strafe * dist
        val maybeCosDist = forward * dist
        val sinYaw = MathHelper.sin(this.rotationYaw.toRadians())
        val cosYaw = MathHelper.cos(this.rotationYaw.toRadians())

        this.motionX += (maybeSinDist * cosYaw - maybeCosDist * sinYaw).toDouble()
        this.motionZ += (maybeCosDist * cosYaw + maybeSinDist * sinYaw).toDouble()
    }
}