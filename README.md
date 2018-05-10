# Android-Dev-UI
Android开发必知必会之UI技能篇（持续更新）

## SimpleViewPager
简单实现一个ViewPager基本功能，基本要点：
- Android中scroll的坐标计算
  - 对一个控件调用scrollBy/scrollTo，真正"滚动"的是该控件的内容，滚动的参考点为该控件的左上角
- Scroller的运用
  - Scroller本身不提供滚动功能，它只负责帮我们收集滚动的坐标数据。我们需要利用收集的数据实现自己的滚动逻辑
