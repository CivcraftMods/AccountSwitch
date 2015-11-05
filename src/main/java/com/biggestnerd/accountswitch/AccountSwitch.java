package com.biggestnerd.accountswitch;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


@Mod(modid="accountswitch", name="Account Switcher", version="v1.0.5")
public class AccountSwitch {

	private static AccountSwitch instance;
	private File accountSave;
	private Minecraft mc;
	private AccountList accountList;
	private AuthenticationHandler authHandler;
	private String currentName;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		mc = Minecraft.getMinecraft();
		File configDir = new File(mc.mcDataDir, "/accountSwitch/");
		if(!configDir.isDirectory()) {
			configDir.mkdir();
		}
		accountSave = new File(configDir, "accounts.json");
		if(!accountSave.isFile()) {
			try {
				accountSave.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		accountList = AccountList.load(accountSave);
		if(accountList == null) {
			accountList = new AccountList();
		}
		accountList.save(accountSave);
		authHandler = new AuthenticationHandler(mc);
		FMLCommonHandler.instance().bus().register(this);
		currentName = mc.getSession().getUsername();
	}
	
	public void useAccount(Account acct) {
		if(acct.getName().equals(currentName) && authHandler.validSession(mc.getSession()))
			return;
		authHandler.setSession(authHandler.makeSession(acct));
	}
	
	public static AccountSwitch getInstance() {
		return instance;
	}
	
	public AccountList getAccountList() {
		return accountList;
	}
	
	public void saveAccounts() {
		accountList.save(accountSave);
	}
	
	public AuthenticationHandler getAuthHandler() {
		return authHandler;
	}
	
	public void setCurrent(String name) {
		currentName = name;
	}
	
	public String getCurrent() {
		return currentName;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(mc.currentScreen instanceof GuiMainMenu) {
			mc.displayGuiScreen(new GuiNewMainMenu());
		}
	}
}
