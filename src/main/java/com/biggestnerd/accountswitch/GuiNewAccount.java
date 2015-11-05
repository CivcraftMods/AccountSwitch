package com.biggestnerd.accountswitch;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiNewAccount extends GuiScreen {

	private final GuiScreen parent;
	private GuiTextField usernameField;
	private GuiTextField passwordField;
	private GuiButton done;
	
	public GuiNewAccount(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		usernameField = new GuiTextField(1, fontRendererObj, this.width / 2 - 100, this.height / 4 - 22, 200, 20);
		passwordField = new GuiTextField(2, fontRendererObj, this.width / 2 - 100, this.height / 4 + 2, 200, 20);
		this.buttonList.add(done = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 36, "Done"));
		usernameField.setFocused(true);
		passwordField.setMaxStringLength(256);
		usernameField.setMaxStringLength(128);
	}
	
	public void updateScreen() {
		boolean validInput = usernameField.getText().trim().length() > 0 && passwordField.getText().trim().length() > 0;
		done.enabled = validInput;
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				AccountSwitch.getInstance().getAuthHandler().validateAccount(usernameField.getText().trim(), passwordField.getText().trim());
				mc.displayGuiScreen(parent);
			}
		}
	}
	
	public void mouseClicked(int x, int y, int button) {
		try {
			super.mouseClicked(x, y, button);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		usernameField.mouseClicked(x, y, button);
		passwordField.mouseClicked(x, y, button);
	}
	
	public void keyTyped(char keyChar, int keyCode) {
		if(keyCode == Keyboard.KEY_TAB) {
			usernameField.setFocused(!usernameField.isFocused());
			passwordField.setFocused(!passwordField.isFocused());
		}
		if(usernameField.isFocused()) {
			usernameField.textboxKeyTyped(keyChar, keyCode);
		}
		if(passwordField.isFocused()) {
			passwordField.textboxKeyTyped(keyChar, keyCode);
		}
		if(keyCode == Keyboard.KEY_RETURN) {
			actionPerformed(done);
		}
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(parent);
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Add Account", this.width / 2, 20, Color.WHITE.getRGB());
		usernameField.drawTextBox();
		passwordField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
