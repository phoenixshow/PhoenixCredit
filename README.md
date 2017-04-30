# PhoenixCredit
    A P2P investment project.

1. 金融信贷类项目，主要代码来自宋红康
2. 使用8.5.1版ButterKnife
3. 底部导航可用RadioButton或FragmentTabHost实现，导航图标和文字状态切换的样式可写Selector实现
4. 当继承Activity时，可通过requestWindowFeature(Window.FEATURE_NO_TITLE);去标题；当继承AppCompatActivity时，可通过隐藏ActionBar去标题
5. Activity的入栈管理只提供了Activity的添加、删除指定/当前/所有、返回单例、栈大小，为空判断较全面，易车还提供了根据Class查找Activity、删除指定Activity之上的所有Activity，代码更优雅
6. 使用1.4.9版AsyncHttpClient
7. 使用1.1.56.android版fastjson
