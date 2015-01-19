package com.wo2b.xxx.webapp.manager.user;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class User implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	private String username;

	private String password;

	private String nickname;

	private String pkgname;

	private String email;

	private String tel;

	private long exp;

	private int roleId = Role.NORMAL;

	private UserGold userGold;

	private String mac; // Mac地址

	private String deviceId; // 唯一的设备id, GSM手机的 IMEI 和 CDMA手机的 MEID.
	
	private boolean isEnable; // 账号是否可用

	private Role role;

	@Override
	public String toString()
	{
		return "User [id=" + id + ", username=" + username + ", pkgname=" + pkgname + "]";
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getPkgname()
	{
		return pkgname;
	}

	public void setPkgname(String pkgname)
	{
		this.pkgname = pkgname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public long getExp()
	{
		return exp;
	}

	public void setExp(long exp)
	{
		this.exp = exp;
	}

	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public int getRoleId()
	{
		return roleId;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}

	public boolean isEnable()
	{
		return isEnable;
	}

	public void setEnable(boolean isEnable)
	{
		this.isEnable = isEnable;
	}

	public UserGold getUserGold()
	{
		return userGold;
	}

	public void setUserGold(UserGold gold)
	{
		this.userGold = gold;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

}
