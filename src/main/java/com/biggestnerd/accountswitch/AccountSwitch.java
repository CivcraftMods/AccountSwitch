package com.biggestnerd.accountswitch;

import java.awt.Color;
import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;


@Mod(modid="accountswitch", name="Account Switcher", version="v1.2.3")
public class AccountSwitch {

	private static AccountSwitch instance;
	private File accountSave;
	private Minecraft mc;
	private AccountList accountList;
	private AuthenticationHandler authHandler;
	private String currentName;
	private boolean validSession = true;
	private long lastSessionCheck = 0;
	private Encrypt encrypt;
	
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
		LegacyAccountList legacyList = LegacyAccountList.load(accountSave);
		accountList = new AccountList();
		if (legacyList == null){
			System.out.println("Not legacy");
			accountList = AccountList.load(accountSave);
			if(accountList == null) {
				accountList = new AccountList();
			}
			accountList.save(accountSave);
		} else {
			accountList = new AccountList();
		}
		authHandler = new AuthenticationHandler(mc);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		currentName = mc.getSession().getUsername();
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

	}
	
	@SubscribeEvent
	public void drawGuiEvent(DrawScreenEvent.Post event) {
		if(event.gui instanceof GuiMainMenu) {
			if(mc.getSession() != null && System.currentTimeMillis() - lastSessionCheck > 60000) {
				lastSessionCheck = System.currentTimeMillis();
				validSession = authHandler.validSession(mc.getSession());
			}
			String display = validSession ? ("Current: " + AccountSwitch.getInstance().getCurrent()) : "Invalid session, restart or switch accounts!";
	        Color color = validSession ? Color.WHITE : Color.RED;
			event.gui.drawString(mc.fontRendererObj, display, 110, 10, color.getRGB());
		}
	}
	
	@SubscribeEvent
	public void initGuiEvent(InitGuiEvent.Post event) {
		if(event.gui instanceof GuiMainMenu) {
			lastSessionCheck = System.currentTimeMillis();
			validSession = authHandler.validSession(mc.getSession());
			event.buttonList.add(new GuiButton(100, 5, 5, 100, 20, "Switch Accounts"));
		}
	}
	
	@SubscribeEvent
	public void actionPerformedEvent(ActionPerformedEvent.Post event) {
		if(event.gui instanceof GuiMainMenu) {
			if(event.button.id == 100) {
				mc.displayGuiScreen(new GuiAccountsList(event.gui));
			}
		}
	}
	
	public void setEncryptionKey(String key, byte[] salt) {
		encrypt = new Encrypt(key, salt);
		saveAccounts();
	}
	
	public Encrypt getEncrypt() {
		return encrypt;
	}
	
	public File getSaveFile() {
		return accountSave;
	}
	
	public void replaceAccountList(AccountList newList) {
		this.accountList = newList;
		newList.save(accountSave);
	}
}
