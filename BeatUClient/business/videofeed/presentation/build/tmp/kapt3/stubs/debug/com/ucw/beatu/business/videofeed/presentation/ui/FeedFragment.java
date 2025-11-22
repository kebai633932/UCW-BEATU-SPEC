package com.ucw.beatu.business.videofeed.presentation.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J&\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u001a\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u00132\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\b\u0010\u001c\u001a\u00020\u0011H\u0002J\u0018\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0006H\u0002J\u0010\u0010 \u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u0004H\u0002J\f\u0010!\u001a\u00020\u0006*\u00020\u0006H\u0002J\f\u0010!\u001a\u00020\u0004*\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/ucw/beatu/business/videofeed/presentation/ui/FeedFragment;", "Landroidx/fragment/app/Fragment;", "()V", "currentPage", "", "followTabCenterX", "", "followTabCenterY", "meTabCenterX", "meTabCenterY", "recommendTabCenterX", "recommendTabCenterY", "tabIndicator", "Lcom/ucw/beatu/business/videofeed/presentation/ui/widget/TabIndicatorView;", "viewPager", "Landroidx/viewpager2/widget/ViewPager2;", "calculateTabPositions", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "setupViewPagerListener", "updateIndicatorOnScroll", "position", "offset", "updateTabSelection", "dpToPx", "TabPagerAdapter", "presentation_debug"})
public final class FeedFragment extends androidx.fragment.app.Fragment {
    private androidx.viewpager2.widget.ViewPager2 viewPager;
    private int currentPage = 1;
    @org.jetbrains.annotations.Nullable()
    private com.ucw.beatu.business.videofeed.presentation.ui.widget.TabIndicatorView tabIndicator;
    private float followTabCenterX = 0.0F;
    private float followTabCenterY = 0.0F;
    private float recommendTabCenterX = 0.0F;
    private float recommendTabCenterY = 0.0F;
    private float meTabCenterX = 0.0F;
    private float meTabCenterY = 0.0F;
    
    public FeedFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * 计算各个Tab的位置（相对于指示器View）
     * X坐标：文字中心
     * Y坐标：文字底部下方（正下方）
     */
    private final void calculateTabPositions() {
    }
    
    /**
     * dp转px的扩展函数
     */
    private final float dpToPx(float $this$dpToPx) {
        return 0.0F;
    }
    
    /**
     * 设置ViewPager2的监听器
     */
    private final void setupViewPagerListener() {
    }
    
    /**
     * 根据滑动进度更新指示器
     * ViewPager2的onPageScrolled中：
     * - position: 当前页面索引（起点）
     * - offset: 滑动到下一个页面的进度（0到1）
     *
     * 逻辑：
     * - 起点和终点都是圆点（progress=0）
     * - 中点位置拉长到最大（progress=1）
     * - 使用sin函数实现平滑的抛物线效果
     */
    private final void updateIndicatorOnScroll(int position, float offset) {
    }
    
    /**
     * 更新顶部菜单栏的选中状态
     * @param position 当前选中的页面索引（0=关注，1=推荐）
     */
    private final void updateTabSelection(int position) {
    }
    
    /**
     * dp转px的扩展函数
     */
    private final int dpToPx(int $this$dpToPx) {
        return 0;
    }
    
    /**
     * ViewPager2的适配器
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\bH\u0016\u00a8\u0006\n"}, d2 = {"Lcom/ucw/beatu/business/videofeed/presentation/ui/FeedFragment$TabPagerAdapter;", "Landroidx/viewpager2/adapter/FragmentStateAdapter;", "fragmentActivity", "Landroidx/fragment/app/FragmentActivity;", "(Landroidx/fragment/app/FragmentActivity;)V", "createFragment", "Landroidx/fragment/app/Fragment;", "position", "", "getItemCount", "presentation_debug"})
    static final class TabPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
        
        public TabPagerAdapter(@org.jetbrains.annotations.NotNull()
        androidx.fragment.app.FragmentActivity fragmentActivity) {
            super(null);
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public androidx.fragment.app.Fragment createFragment(int position) {
            return null;
        }
    }
}