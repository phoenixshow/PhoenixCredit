# PhoenixCredit
    A P2P investment project.

1. 金融信贷类项目，主要代码来自宋红康
2. 使用8.5.1版ButterKnife
3. 底部导航可用RadioButton或FragmentTabHost实现，导航图标和文字状态切换的样式可写Selector实现
4. 当继承Activity时，可通过requestWindowFeature(Window.FEATURE_NO_TITLE);去标题；当继承AppCompatActivity时，可通过隐藏ActionBar去标题
5. Activity的入栈管理只提供了Activity的添加、删除指定/当前/所有、返回单例、栈大小，为空判断较全面，易车还提供了根据Class查找Activity、删除指定Activity之上的所有Activity，代码更优雅
6. 使用1.4.9版AsyncHttpClient，对比旧版本“switched library from using org.apache.http to use cz.msebera.android.httpclient”
7. 使用1.1.56.android版fastjson
8. 使用移动支付版支付宝SDK
9. 使用2.8.3版ShareSDK
10. 使用3.0.2版MPAndroidChart
11. 添加了动态授权
12. 针对Android N对访问文件权限收回，APK更新安装时使用 FileProvider 授予 URI 临时访问权限
