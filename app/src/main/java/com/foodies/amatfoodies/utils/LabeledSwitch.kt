package com.foodies.amatfoodies.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

import com.foodies.amatfoodies.R


class LabeledSwitch : ToggleableView {
    private var padding: Int = 0

    private var colorOn: Int = 0
    private var colorOff: Int = 0
    private var colorBorder: Int = 0
    private var colorDisabled: Int = 0

    private var textSize: Int = 0

    private var outerRadii: Int = 0
    private var thumbRadii: Int = 0

    private var paint: Paint? = null

    private var startTime: Long = 0

    private var labelOn: String? = null
    private var labelOff: String? = null

    private var thumbBounds: RectF? = null

    private var leftBgArc: RectF? = null
    private var rightBgArc: RectF? = null

    private var leftFgArc: RectF? = null
    private var rightFgArc: RectF? = null

    var typeface: Typeface? = null
        set(typeface) {
            field = typeface
            paint!!.typeface = typeface
            invalidate()
        }

    private var thumbOnCenterX: Float = 0.toFloat()
    private var thumbOffCenterX: Float = 0.toFloat()
    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
        initProperties(attrs)
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
        initProperties(attrs)
    }

    private fun initView() {
        this.isOn = false
        this.labelOn = "Offline"
        this.labelOff = "Go Offline"

        this.enabled = true
        this.textSize = (12f * resources.displayMetrics.scaledDensity).toInt()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorOn = resources.getColor(R.color.tb_colorPrimary, context.theme)
            colorBorder = colorOn
        } else {
            colorOn = resources.getColor(R.color.tb_view_color)
            colorBorder = colorOn
        }

        paint = Paint()
        paint!!.isAntiAlias = true

        leftBgArc = RectF()
        rightBgArc = RectF()

        leftFgArc = RectF()
        rightFgArc = RectF()
        thumbBounds = RectF()

        this.colorOff = Color.parseColor("#FFFFFF")
        this.colorDisabled = Color.parseColor("#D3D3D3")
    }

    private fun initProperties(attrs: AttributeSet) {
        val tarr = context.theme.obtainStyledAttributes(attrs, R.styleable.Toggle, 0, 0)
        val N = tarr.indexCount
        for (i in 0 until N) {
            val attr = tarr.getIndex(i)
            if (attr == R.styleable.Toggle_on) {
                isOn = tarr.getBoolean(R.styleable.Toggle_on, false)
            } else if (attr == R.styleable.Toggle_colorOff) {
                colorOff = tarr.getColor(R.styleable.Toggle_colorOff, Color.parseColor("#FFFFFF"))
            } else if (attr == R.styleable.Toggle_colorBorder) {
                val accentColor: Int
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    accentColor = resources.getColor(R.color.tb_colorPrimary, context.theme)
                } else {
                    accentColor = resources.getColor(R.color.tb_colorPrimary)
                }
                colorBorder = tarr.getColor(R.styleable.Toggle_colorBorder, accentColor)
            } else if (attr == R.styleable.Toggle_colorOn) {
                val accentColor: Int
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    accentColor = resources.getColor(R.color.tb_colorPrimary, context.theme)
                } else {
                    accentColor = resources.getColor(R.color.tb_colorPrimary)
                }
                colorOn = tarr.getColor(R.styleable.Toggle_colorOn, accentColor)
            } else if (attr == R.styleable.Toggle_colorDisabled) {
                colorDisabled =
                    tarr.getColor(R.styleable.Toggle_colorOff, Color.parseColor("#D3D3D3"))
            } else if (attr == R.styleable.Toggle_textOff) {
                labelOff = tarr.getString(R.styleable.Toggle_textOff)
            } else if (attr == R.styleable.Toggle_textOn) {
                labelOn = tarr.getString(R.styleable.Toggle_textOn)
            } else if (attr == R.styleable.Toggle_android_textSize) {
                val defaultTextSize = (12f * resources.displayMetrics.scaledDensity).toInt()
                textSize =
                    tarr.getDimensionPixelSize(R.styleable.Toggle_android_textSize, defaultTextSize)
            } else if (attr == R.styleable.Toggle_android_enabled) {
                enabled = tarr.getBoolean(R.styleable.Toggle_android_enabled, false)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint!!.textSize = textSize.toFloat()

        //      Drawing Switch background here
        run {
            if (isEnabled) {
                paint!!.color = colorBorder
            } else {
                paint!!.color = colorDisabled
            }
            canvas.drawArc(leftBgArc!!, 90f, 180f, false, paint!!)
            canvas.drawArc(rightBgArc!!, 90f, -180f, false, paint!!)
            canvas.drawRect(
                outerRadii.toFloat(),
                0f,
                (width - outerRadii).toFloat(),
                height.toFloat(),
                paint!!
            )

            paint!!.color = colorOff

            canvas.drawArc(leftFgArc!!, 90f, 180f, false, paint!!)
            canvas.drawArc(rightFgArc!!, 90f, -180f, false, paint!!)
            canvas.drawRect(
                outerRadii.toFloat(),
                (padding / 10).toFloat(),
                (width - outerRadii).toFloat(),
                (height - padding / 10).toFloat(),
                paint!!
            )

            var alpha =
                ((thumbBounds!!.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val onColor: Int

            if (isEnabled) {
                onColor =
                    Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn))
            } else {
                onColor = Color.argb(
                    alpha,
                    Color.red(colorDisabled),
                    Color.green(colorDisabled),
                    Color.blue(colorDisabled)
                )
            }
            paint!!.color = onColor

            canvas.drawArc(leftBgArc!!, 90f, 180f, false, paint!!)
            canvas.drawArc(rightBgArc!!, 90f, -180f, false, paint!!)
            canvas.drawRect(
                outerRadii.toFloat(),
                0f,
                (width - outerRadii).toFloat(),
                height.toFloat(),
                paint!!
            )

            alpha =
                ((thumbOnCenterX - thumbBounds!!.centerX()) / (thumbOnCenterX - thumbOffCenterX) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val offColor =
                Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff))
            paint!!.color = offColor

            canvas.drawArc(leftFgArc!!, 90f, 180f, false, paint!!)
            canvas.drawArc(rightFgArc!!, 90f, -180f, false, paint!!)
            canvas.drawRect(
                outerRadii.toFloat(),
                (padding / 10).toFloat(),
                (width - outerRadii).toFloat(),
                (height - padding / 10).toFloat(),
                paint!!
            )
        }

        //      Drawing Switch Labels here
        val MAX_CHAR = "N"
        val textCenter = paint!!.measureText(MAX_CHAR) / 2
        if (isOn) {
            var alpha =
                ((width.ushr(1) - thumbBounds!!.centerX()) / (width.ushr(1) - thumbOffCenterX) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val onColor =
                Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn))
            paint!!.color = Color.TRANSPARENT

            var centerX =
                (width - padding - (padding + padding.ushr(1) + (thumbRadii shl 1))).ushr(1)
                    .toFloat()
            canvas.drawText(
                labelOff!!,
                (padding + padding.ushr(1)).toFloat() + (thumbRadii shl 1).toFloat() + centerX - paint!!.measureText(
                    labelOff
                ) / 2,
                height.ushr(1) + textCenter,
                paint!!
            )

            alpha =
                ((thumbBounds!!.centerX() - width.ushr(1)) / (thumbOnCenterX - width.ushr(1)) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val offColor =
                Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff))
            paint!!.color = Color.WHITE

            val maxSize = width - (padding shl 1) - (thumbRadii shl 1)

            centerX = (padding.ushr(1) + maxSize - padding).ushr(1).toFloat()
            canvas.drawText(
                labelOn!!,
                padding + centerX - paint!!.measureText(labelOn) / 2,
                height.ushr(1) + textCenter,
                paint!!
            )
        } else {
            var alpha =
                ((thumbBounds!!.centerX() - width.ushr(1)) / (thumbOnCenterX - width.ushr(1)) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val offColor =
                Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff))
            paint!!.color = offColor

            val maxSize = width - (padding shl 1) - (thumbRadii shl 1)
            var centerX = (padding.ushr(1) + maxSize - padding).ushr(1).toFloat()
            canvas.drawText(
                labelOn!!,
                padding + centerX - paint!!.measureText(labelOn) / 2,
                height.ushr(1) + textCenter,
                paint!!
            )

            alpha =
                ((width.ushr(1) - thumbBounds!!.centerX()) / (width.ushr(1) - thumbOffCenterX) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val onColor: Int
            if (isEnabled) {
                onColor =
                    Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn))
            } else {
                onColor = Color.argb(
                    alpha,
                    Color.red(colorDisabled),
                    Color.green(colorDisabled),
                    Color.blue(colorDisabled)
                )
            }
            paint!!.color = Color.WHITE

            centerX = (width - padding - (padding + padding.ushr(1) + (thumbRadii shl 1))).ushr(1)
                .toFloat()
            canvas.drawText(
                labelOff!!,
                (padding + padding.ushr(1)).toFloat() + (thumbRadii shl 1).toFloat() + centerX - paint!!.measureText(
                    labelOff
                ) / 2,
                height.ushr(1) + textCenter,
                paint!!
            )
        }

        //      Drawing Switch Thumb here
        run {
            var alpha =
                ((thumbBounds!!.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val offColor =
                Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff))
            paint!!.color = Color.WHITE

            canvas.drawCircle(
                thumbBounds!!.centerX(),
                thumbBounds!!.centerY(),
                thumbRadii.toFloat(),
                paint!!
            )
            alpha =
                ((thumbOnCenterX - thumbBounds!!.centerX()) / (thumbOnCenterX - thumbOffCenterX) * 255).toInt()
            alpha = if (alpha < 0) 0 else if (alpha > 255) 255 else alpha
            val onColor: Int
            if (isEnabled) {
                onColor =
                    Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn))
            } else {
                onColor = Color.argb(
                    alpha,
                    Color.red(colorDisabled),
                    Color.green(colorDisabled),
                    Color.blue(colorDisabled)
                )
            }
            paint!!.color = Color.WHITE
            canvas.drawCircle(
                thumbBounds!!.centerX(),
                thumbBounds!!.centerY(),
                thumbRadii.toFloat(),
                paint!!
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = resources.getDimensionPixelSize(R.dimen._90sdp)
        val desiredHeight = resources.getDimensionPixelSize(R.dimen._20sdp)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize)
        } else {
            width = desiredWidth
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize)
        } else {
            height = desiredHeight
        }

        setMeasuredDimension(width, height)

        outerRadii = Math.min(width, height).ushr(1)
        thumbRadii = (Math.min(width, height) / 2.88f).toInt()
        padding = (height - thumbRadii).ushr(1)

        thumbBounds!!.set(
            (width - padding - thumbRadii).toFloat(),
            padding.toFloat(),
            (width - padding).toFloat(),
            (height - padding).toFloat()
        )
        thumbOnCenterX = thumbBounds!!.centerX()

        thumbBounds!!.set(
            padding.toFloat(),
            padding.toFloat(),
            (padding + thumbRadii).toFloat(),
            (height - padding).toFloat()
        )
        thumbOffCenterX = thumbBounds!!.centerX()

        if (isOn) {
            thumbBounds!!.set(
                (width - padding - thumbRadii).toFloat(),
                padding.toFloat(),
                (width - padding).toFloat(),
                (height - padding).toFloat()
            )
        } else {
            thumbBounds!!.set(
                padding.toFloat(),
                padding.toFloat(),
                (padding + thumbRadii).toFloat(),
                (height - padding).toFloat()
            )
        }

        leftBgArc!!.set(0f, 0f, (outerRadii shl 1).toFloat(), height.toFloat())
        rightBgArc!!.set(
            (width - (outerRadii shl 1)).toFloat(),
            0f,
            width.toFloat(),
            height.toFloat()
        )

        leftFgArc!!.set(
            (padding / 10).toFloat(),
            (padding / 10).toFloat(),
            ((outerRadii shl 1) - padding / 10).toFloat(),
            (height - padding / 10).toFloat()
        )
        rightFgArc!!.set(
            (width - (outerRadii shl 1) + padding / 10).toFloat(),
            (padding / 10).toFloat(),
            (width - padding / 10).toFloat(),
            (height - padding / 10).toFloat()
        )
    }

    /**
     * Call this view's OnClickListener, if it is defined.  Performs all normal
     * actions associated with clicking: reporting accessibility event, playing
     * a sound, etc.
     *
     * @return True there was an assigned OnClickListener that was called, false
     * otherwise is returned.
     */
    override fun performClick(): Boolean {
        super.performClick()
        if (isOn) {
            val switchColor = ValueAnimator.ofFloat(width.toFloat() - padding - thumbRadii, padding.toFloat())
            switchColor.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                thumbBounds!!.set(
                    value,
                    thumbBounds!!.top,
                    value + thumbRadii,
                    thumbBounds!!.bottom
                )
                invalidate()
            }
            switchColor.interpolator = AccelerateDecelerateInterpolator()
            switchColor.duration = 250
            switchColor.start()
        } else {
            val switchColor = ValueAnimator.ofFloat(padding.toFloat(), (width - padding - thumbRadii).toFloat())
            switchColor.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                thumbBounds!!.set(
                    value,
                    thumbBounds!!.top,
                    value + thumbRadii,
                    thumbBounds!!.bottom
                )
                invalidate()
            }
            switchColor.interpolator = AccelerateDecelerateInterpolator()
            switchColor.duration = 250
            switchColor.start()
        }
        isOn = !isOn
        if (onToggledListener != null) {
            onToggledListener.onSwitched(this, isOn)
        }
        return true
    }

    /**
     * Method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled) {
            val x = event.x
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startTime = System.currentTimeMillis()
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (x - thumbRadii.ushr(1) > padding && x + thumbRadii.ushr(1) < width - padding) {
                        thumbBounds!!.set(
                            x - thumbRadii.ushr(1),
                            thumbBounds!!.top,
                            x + thumbRadii.ushr(1),
                            thumbBounds!!.bottom
                        )
                        invalidate()
                    }
                    return true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val endTime = System.currentTimeMillis()
                    val span = endTime - startTime
                    if (span < 200) {
                        performClick()
                    } else {
                        if (x >= width.ushr(1)) {
                            val switchColor = ValueAnimator.ofFloat(
                                if (x > width - padding - thumbRadii) (width - padding - thumbRadii).toFloat() else x,
                                ( width - padding - thumbRadii).toFloat()
                            )
                            switchColor.addUpdateListener { animation ->
                                val value = animation.animatedValue as Float
                                thumbBounds!!.set(
                                    value,
                                    thumbBounds!!.top,
                                    value + thumbRadii,
                                    thumbBounds!!.bottom
                                )
                                invalidate()
                            }
                            switchColor.interpolator = AccelerateDecelerateInterpolator()
                            switchColor.duration = 250
                            switchColor.start()
                            isOn = true
                        } else {
                            val switchColor =
                                ValueAnimator.ofFloat(if (x < padding) padding.toFloat() else x, padding.toFloat())
                            switchColor.addUpdateListener { animation ->
                                val value = animation.animatedValue as Float
                                thumbBounds!!.set(
                                    value,
                                    thumbBounds!!.top,
                                    value + thumbRadii,
                                    thumbBounds!!.bottom
                                )
                                invalidate()
                            }
                            switchColor.interpolator = AccelerateDecelerateInterpolator()
                            switchColor.duration = 250
                            switchColor.start()
                            isOn = false
                        }
                        if (onToggledListener != null) {
                            onToggledListener.onSwitched(this, isOn)
                        }
                    }
                    invalidate()
                    return true
                }

                else -> {
                    return super.onTouchEvent(event)
                }
            }
        } else {
            return false
        }
    }

    /**
     *
     * Returns the color value for colorOn.
     *
     * @return color value for label and thumb in off state and background in on state.
     */
    fun getColorOn(): Int {
        return colorOn
    }

    /**
     *
     * Changes the on color value of this Switch.
     *
     * @param colorOn color value for label and thumb in off state and background in on state.
     */
    fun setColorOn(colorOn: Int) {
        this.colorOn = colorOn
        invalidate()
    }

    /**
     *
     * Returns the color value for colorOff.
     *
     * @return color value for label and thumb in on state and background in off state.
     */
    fun getColorOff(): Int {
        return colorOff
    }

    /**
     *
     * Changes the off color value of this Switch.
     *
     * @param colorOff color value for label and thumb in on state and background in off state.
     */
    fun setColorOff(colorOff: Int) {
        this.colorOff = colorOff
        invalidate()
    }

    /**
     *
     * Returns text label when switch is in on state.
     *
     * @return text label when switch is in on state.
     */
    fun getLabelOn(): String? {
        return labelOn
    }

    /**
     *
     * Changes text label when switch is in on state.
     *
     * @param labelOn text label when switch is in on state.
     */
    fun setLabelOn(labelOn: String) {
        this.labelOn = labelOn
        invalidate()
    }

    /**
     *
     * Returns text label when switch is in off state.
     *
     * @return text label when switch is in off state.
     */
    fun getLabelOff(): String? {
        return labelOff
    }

    /**
     *
     * Changes text label when switch is in off state.
     *
     * @param labelOff text label when switch is in off state.
     */
    fun setLabelOff(labelOff: String) {
        this.labelOff = labelOff
        invalidate()
    }

    /**
     *
     * Changes the boolean state of this Switch.
     *
     * @param on true to turn switch on, false to turn it off.
     */
    override fun setOn(on: Boolean) {
        super.setOn(on)
        if (isOn) {
            thumbBounds!!.set(
                (width - padding - thumbRadii).toFloat(),
                padding.toFloat(),
                (width - padding).toFloat(),
                (height - padding).toFloat()
            )
        } else {
            thumbBounds!!.set(
                padding.toFloat(),
                padding.toFloat(),
                (padding + thumbRadii).toFloat(),
                (height - padding).toFloat()
            )
        }
        invalidate()
    }

    /**
     *
     * Returns the color value for Switch disabled state.
     *
     * @return color value used by background, border and thumb when switch is disabled.
     */
    fun getColorDisabled(): Int {
        return colorDisabled
    }

    /**
     *
     * Changes the color value for Switch disabled state.
     *
     * @param colorDisabled color value used by background, border and thumb when switch is disabled.
     */
    fun setColorDisabled(colorDisabled: Int) {
        this.colorDisabled = colorDisabled
        invalidate()
    }

    /**
     *
     * Returns the color value for Switch border.
     *
     * @return color value used by Switch border.
     */
    fun getColorBorder(): Int {
        return colorBorder
    }

    /**
     *
     * Changes the color value for Switch disabled state.
     *
     * @param colorBorder color value used by Switch border.
     */
    fun setColorBorder(colorBorder: Int) {
        this.colorBorder = colorBorder
        invalidate()
    }

    /**
     *
     * Returns the text size for Switch on/off label.
     *
     * @return text size for Switch on/off label.
     */
    fun getTextSize(): Int {
        return textSize
    }

    /**
     *
     * Changes the text size for Switch on/off label.
     *
     * @param textSize text size for Switch on/off label.
     */
    fun setTextSize(textSize: Int) {
        this.textSize = (textSize * resources.displayMetrics.scaledDensity).toInt()
        invalidate()
    }
}
