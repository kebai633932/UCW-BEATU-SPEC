package com.ucw.beatu.business.videofeed.presentation.ui.widget;

/**
 * 水滴形状的Tab指示器
 * 默认状态：小圆点
 * 滑动时：像水滴一样拉长
 * 切换完成后：聚合成小圆点
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010#\u001a\u00020$J(\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\f2\u0006\u0010)\u001a\u00020\f2\u0006\u0010*\u001a\u00020\fH\u0002J\u000e\u0010+\u001a\u00020$2\u0006\u0010,\u001a\u00020\u0007J\u0010\u0010-\u001a\u00020$2\u0006\u0010&\u001a\u00020\'H\u0014J\u0006\u0010.\u001a\u00020$J6\u0010/\u001a\u00020$2\u0006\u0010\u0014\u001a\u00020\f2\u0006\u0010!\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001d\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\fJ6\u00100\u001a\u00020$2\u0006\u00101\u001a\u00020\f2\u0006\u00102\u001a\u00020\f2\u0006\u00103\u001a\u00020\f2\u0006\u00104\u001a\u00020\f2\u0006\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020\fJ\u0006\u00107\u001a\u00020$J\f\u00108\u001a\u00020\f*\u00020\fH\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b\u001a\u0010\u0016R\u001e\u0010\u001b\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b\u001c\u0010\u0016R\u001e\u0010\u001d\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b\u001e\u0010\u0016R\u001e\u0010\u001f\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b \u0010\u0016R\u001e\u0010!\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\f@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b\"\u0010\u0016\u00a8\u00069"}, d2 = {"Lcom/ucw/beatu/business/videofeed/presentation/ui/widget/TabIndicatorView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "animator", "Landroid/animation/ValueAnimator;", "dotRadius", "", "followTabX", "followTabY", "meTabX", "meTabY", "paint", "Landroid/graphics/Paint;", "value", "positionProgress", "setPositionProgress", "(F)V", "recommendTabX", "recommendTabY", "startX", "setStartX", "startY", "setStartY", "targetX", "setTargetX", "targetY", "setTargetY", "widthProgress", "setWidthProgress", "animateToTarget", "", "drawDropletShape", "canvas", "Landroid/graphics/Canvas;", "centerX", "centerY", "progress", "moveToTab", "tabIndex", "onDraw", "resetToDot", "setScrollProgress", "setTabPositions", "followX", "followY", "recommendX", "recommendY", "meX", "meY", "setToDot", "dpToPx", "presentation_debug"})
public final class TabIndicatorView extends android.view.View {
    @org.jetbrains.annotations.NotNull()
    private final android.graphics.Paint paint = null;
    private final float dotRadius = 0.0F;
    private float positionProgress = 0.0F;
    private float widthProgress = 0.0F;
    private float targetX = 0.0F;
    private float targetY = 0.0F;
    private float startX = 0.0F;
    private float startY = 0.0F;
    private float followTabX = 0.0F;
    private float followTabY = 0.0F;
    private float recommendTabX = 0.0F;
    private float recommendTabY = 0.0F;
    private float meTabX = 0.0F;
    private float meTabY = 0.0F;
    @org.jetbrains.annotations.Nullable()
    private android.animation.ValueAnimator animator;
    
    @kotlin.jvm.JvmOverloads()
    public TabIndicatorView(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public TabIndicatorView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public TabIndicatorView(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyleAttr) {
        super(null);
    }
    
    private final void setPositionProgress(float value) {
    }
    
    private final void setWidthProgress(float value) {
    }
    
    private final void setTargetX(float value) {
    }
    
    private final void setTargetY(float value) {
    }
    
    private final void setStartX(float value) {
    }
    
    private final void setStartY(float value) {
    }
    
    @java.lang.Override()
    protected void onDraw(@org.jetbrains.annotations.NotNull()
    android.graphics.Canvas canvas) {
    }
    
    /**
     * 绘制水滴形状
     * @param canvas 画布
     * @param centerX 中心X坐标
     * @param centerY 中心Y坐标
     * @param progress 拉长进度（0-1）
     */
    private final void drawDropletShape(android.graphics.Canvas canvas, float centerX, float centerY, float progress) {
    }
    
    /**
     * 设置滑动进度
     * @param positionProgress 位置进度值（0-1），用于计算指示器的位置，0表示在起点，1表示在终点
     * @param widthProgress 宽度进度值（0-1），用于计算指示器的长度，0表示圆点状态，1表示完全拉长状态
     * @param startX 起始位置的中心X坐标（相对于指示器View）
     * @param startY 起始位置的中心Y坐标（相对于指示器View）
     * @param targetX 目标位置的中心X坐标（相对于指示器View）
     * @param targetY 目标位置的中心Y坐标（相对于指示器View）
     */
    public final void setScrollProgress(float positionProgress, float widthProgress, float startX, float startY, float targetX, float targetY) {
    }
    
    /**
     * 动画到目标位置（切换完成后聚合成圆点）
     */
    public final void animateToTarget() {
    }
    
    /**
     * 重置为圆点状态
     */
    public final void resetToDot() {
    }
    
    /**
     * 直接设置为圆点状态（无动画）
     */
    public final void setToDot() {
    }
    
    /**
     * 设置Tab位置（用于初始化）
     * @param followX 关注Tab的中心X坐标（相对于指示器View）
     * @param followY 关注Tab的中心Y坐标（相对于指示器View）
     * @param recommendX 推荐Tab的中心X坐标（相对于指示器View）
     * @param recommendY 推荐Tab的中心Y坐标（相对于指示器View）
     * @param meX 我Tab的中心X坐标（相对于指示器View）
     * @param meY 我Tab的中心Y坐标（相对于指示器View）
     */
    public final void setTabPositions(float followX, float followY, float recommendX, float recommendY, float meX, float meY) {
    }
    
    /**
     * 移动到指定Tab位置（用于页面切换完成时）
     */
    public final void moveToTab(int tabIndex) {
    }
    
    /**
     * dp转px
     */
    private final float dpToPx(float $this$dpToPx) {
        return 0.0F;
    }
}