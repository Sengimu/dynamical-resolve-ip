# dynamical-resolve-ip

### 一个使域名动态解析IP的小工具 (仅支持cloudflare)

运营商开通公网后,由于是动态IP,每次重拨都会导致IP变动,即是解决此问题.

使用前请先增加一条解析记录,记住记录的name值,稍后在配置中需要填写完整域名.

配置完后程序会以定时任务运行,每五分钟进行一次同步,将cloudflare上的解析记录值更新为本机公网IP.

### 使用方法

1.在release中下载jar文件

2.使用Java环境启动

3.第一次打开会生成配置文件并关闭

配置文件如下,照注释填写即可:

    info:
        //cloudflare 邮箱
        email:
        //cloudflare Global API
        GKey:
        //目标域名,就是需要解析的域名,填完整如 www.example.com.
        //若是@无需增加三级域名,直接填写 example.com即可
        name:
        //解析类型
        type: A