package com.biggestnerd.accountswitch;

import java.awt.Color;
import java.security.SecureRandom;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiSetEncryptionKey extends GuiScreen {

	private final GuiScreen parent;
	private GuiButton save;
	private GuiTextField keyField;
	
	public GuiSetEncryptionKey(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(save = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 4, "Set Key"));
		keyField = new GuiTextField(1, mc.fontRendererObj, this.width / 2 - 100, this.height / 4 - 22, 200, 20);
		keyField.setFocused(true);
		keyField.setMaxStringLength(16);
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	public void updateScreen() {
		boolean validInput = keyField.getText().trim().length() > 0;
		save.enabled = validInput;
	}
	
	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				byte[] salt = new byte[8];
				if(AccountSwitch.getInstance().getAccountList().getSalt() == null) {
					SecureRandom secRand = new SecureRandom();
					secRand.nextBytes(salt);
				} else {
					salt = AccountSwitch.getInstance().getAccountList().getSalt();
				}
				AccountSwitch.getInstance().setEncryptionKey(keyField.getText().trim(), salt);
				AccountSwitch.getInstance().saveAccounts();
				LegacyAccountList legacy = LegacyAccountList.load(AccountSwitch.getInstance().getSaveFile());
				if(legacy != null) {
					legacy.migrate();
					AccountSwitch.getInstance().saveAccounts();
				}
				mc.displayGuiScreen(parent);
			}
		}
	}
	
	public void keyTyped(char keyChar, int keyCode) {
		if(keyCode == Keyboard.KEY_RETURN) {
			actionPerformed(save);
		}
		if(keyField.isFocused()) {
			keyField.textboxKeyTyped(keyChar, keyCode);
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		keyField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Set Encryption Key", this.width / 2, 20, Color.WHITE.getRGB());
		keyField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
