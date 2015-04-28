package com.wo2b.tu123.business.base;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class BusinessData
{
	
	/**
	 * 本地相册关注的相册信息
	 */
	public static final String[][] LOCAL_ALBUM_FOCUS = new String[][]
	{
		// 关键路径, 父级名称, 显示名称, 图标
		{ "/DCIM/Camera/", "Camera", "相机照片", "assets://album_focus_icons/system_camera.png" },
		{ "/androidesk/wallpapers/", "wallpapers", "壁纸", "assets://album_focus_icons/system_wallpaper.png" },
		{ "/Pictures/Screenshots/", "Screenshots", "屏幕截图", "assets://album_focus_icons/system_screenshots.png" }, 
		{ "/Tencent/QQ_Images/", "QQ_Images", "QQ照片", "assets://album_focus_icons/tencent_qq_images.png" }, 
		{ "/Tencent/QzonePic/", "QzonePic", "QQ空间", "assets://album_focus_icons/tencent_qzone_images.png" },
		{ "/tieba/", "tieba", "百度贴吧", "assets://album_focus_icons/tieba_images.png" }, 
		{ "/netease/newsreader/netease_down_pic/", "netease_down_pic", "网易新闻", "assets://album_focus_icons/netease_images.png" },
		{ "/UCDownloads/", "UCDownloads", "UC浏览器", "assets://album_focus_icons/uc_images.png" },
		{ "/tianya/Picture/", "Picture", "天涯照片", "assets://album_focus_icons/tianya_images.png" },
		
		
		//{ "/tencent/weiyun/", "微云相册", "微云相册", "assets://album_focus_icons/tencent_weiyun.png" },
		//{ "/Pictures/PIP_CAMERA/", "PIP_CAMERA", "画中画相机", "assets://album_focus_icons/tencent_pip_camera.png" }, 
	};
	
	/**
	 * 花心总数: 即用户可添加关注的路径. 12个表示12个月.
	 */
	public static final int MAX_LOCAL_FOCUS_COUNT = 12;
	
	
	
	
	/**
	 * Modified: 2014-3-12 21:34:21 
	 * 修改直接基于文件目录
	 */
	public static final String[][] LOCAL_FOCUS_PARENT_DIRECTORY = new String[][] {
		// 关键路径, 显示名称, 图标
		
		// 相机照片
		{ "/DCIM/Camera", "相机照片", "assets://album_focus_icons/system_camera.png" },
		
		// 屏幕截图
		{ "/Pictures/Screenshots", "屏幕截图", "assets://album_focus_icons/system_screenshots.png" },
		
		// QQ图片
		{ "/tencent/QQ_Images", "QQ照片", "assets://album_focus_icons/tencent_qq_images.png" },
		
		// QQ空间
		{ "/tencent/QzonePic", "QQ空间", "assets://album_focus_icons/tencent_qzone_images.png" },
		
		// 网易新闻
		{ "/netease/newsreader/netease_down_pic", "网易新闻", "assets://album_focus_icons/netease_images.png" },
		
		// 百度贴吧
		{ "/tieba", "百度贴吧", "assets://album_focus_icons/tieba_images.png" }, 
		
		// UC浏览器
		{ "/UCDownloads", "UC浏览器", "assets://album_focus_icons/uc_images.png" },
		
		// 天涯
		{ "/tianya/Picture", "天涯照片", "assets://album_focus_icons/tianya_images.png" }
		
	};
	
	
	
	
	
	
	
	
	
}
