package com.biggestnerd.accountswitch;

import java.awt.Color;

import com.biggestnerd.accountswitch.AccountSwitchException.ErrorType;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class GuiAccountSwitchError extends GuiScreen {

	private final GuiScreen parent;
	private final AccountSwitchException ex;
	
	public GuiAccountSwitchError(GuiScreen parent, AccountSwitchException ex) {
		this.parent = parent;
		this.ex = ex;
	}
	
	public void initGui() {
		this.buttonList.clear();
		String buttonName = ex.getErrorType() == ErrorType.ENCRYPT ? "Change Encryption Key" : "Done";
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2, buttonName));
	}
	
	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				if(ex.getErrorType() == ErrorType.AUTH) {
					mc.displayGuiScreen(new GuiAccountsList(new GuiMainMenu()));
				} 
				if(ex.getErrorType() == ErrorType.ENCRYPT) {
					mc.displayGuiScreen(new GuiSetEncryptionKey(new GuiAccountsList(new GuiMainMenu())));
				}
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, ex.getMessage(), this.width / 2, this.height / 2 - 20, Color.RED.getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
