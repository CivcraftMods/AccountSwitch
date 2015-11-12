package com.biggestnerd.accountswitch;

import java.awt.Color;
import java.security.SecureRandom;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiChangeEncryptionKey extends GuiScreen {

	private final GuiScreen parent;
	private GuiButton save;
	private GuiTextField oldKeyField;
	private GuiTextField newKeyField;
	
	public GuiChangeEncryptionKey(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(save = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 66, "Set Key"));
		oldKeyField = new GuiTextField(1, mc.fontRendererObj, this.width / 2 - 100, this.height / 4 - 10, 200, 20);
		oldKeyField.setFocused(true);
		oldKeyField.setMaxStringLength(16);
		newKeyField = new GuiTextField(2, mc.fontRendererObj, this.width / 2 - 100, this.height / 4 + 25, 200, 20);
		newKeyField.setMaxStringLength(16);
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	public void updateScreen() {
		boolean validInput = oldKeyField.getText().trim().length() > 0 && newKeyField.getText().trim().length() > 0;
		save.enabled = validInput;
	}
	
	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				if(oldKeyField.getText().trim().equals(AccountSwitch.getInstance().getEncrypt().getCurrentKey())) {
					SecureRandom secRand = new SecureRandom();
					byte[] salt = new byte[8];
					secRand.nextBytes(salt);
					Encrypt newEncrypt = new Encrypt(newKeyField.getText().trim(), salt);
					for(Account acct : AccountSwitch.getInstance().getAccountList().getAccounts()) {
						acct.changeEncryption(newEncrypt);
					}
					AccountSwitch.getInstance().getAccountList().setSalt(salt);
					AccountSwitch.getInstance().saveAccounts();
					AccountSwitch.getInstance().setEncryptionKey(newKeyField.getText().trim(), salt);
				}
				mc.displayGuiScreen(parent);
			}
		}
	}
	
	public void keyTyped(char keyChar, int keyCode) {
		if(keyCode == Keyboard.KEY_RETURN) {
			actionPerformed(save);
		}
		if(keyCode == Keyboard.KEY_TAB) {
			oldKeyField.setFocused(!oldKeyField.isFocused());
			newKeyField.setFocused(!newKeyField.isFocused());
		}
		if(oldKeyField.isFocused()) {
			oldKeyField.textboxKeyTyped(keyChar, keyCode);
		}
		if(newKeyField.isFocused()) {
			newKeyField.textboxKeyTyped(keyChar, keyCode);
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		oldKeyField.mouseClicked(mouseX, mouseY, mouseButton);
		newKeyField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Change Encryption Key", this.width / 2, 20, Color.WHITE.getRGB());
		drawString(fontRendererObj, "Old Key", this.width / 2 - 100, this.height / 4 - 20, Color.WHITE.getRGB());
		oldKeyField.drawTextBox();
		drawString(fontRendererObj, "New Key", this.width / 2 - 100, this.height / 4 + 15, Color.WHITE.getRGB());
		newKeyField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
