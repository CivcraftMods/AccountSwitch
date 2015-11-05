package com.biggestnerd.accountswitch;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiAccountsList extends GuiScreen {
	
	private final GuiScreen parent;
	private ArrayList<Account> accounts;
	private int selected = -1;
	private GuiButton addAccountButton;
	private GuiButton deleteAccountButton;
	private GuiButton useAccountButton;
	private AccountSlot accountListContainer;
	
	public GuiAccountsList(GuiScreen parent) {
		this.parent = parent;
	}
	
	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(addAccountButton = new GuiButton(0, this.width / 2 - 100, this.height - 63, 64, 20, "Add"));
		this.buttonList.add(useAccountButton = new GuiButton(1, this.width / 2 - 32, this.height - 63, 64, 20, "Use"));
		this.buttonList.add(deleteAccountButton = new GuiButton(2, this.width / 2 + 36, this.height - 63, 64, 20, "Delete"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height - 42, "Done"));
		this.accountListContainer = new AccountSlot(this.mc);
		useAccountButton.enabled = false;
		deleteAccountButton.enabled = false;
		this.accounts = AccountSwitch.getInstance().getAccountList().getAccounts();
	}
	
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.accountListContainer.handleMouseInput();
	}
	
	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				mc.displayGuiScreen(new GuiNewAccount(this));
			}
			if(button.id == 1) {
				AccountSwitch.getInstance().useAccount(accounts.get(selected));
				mc.displayGuiScreen(parent);
			}
			if(button.id == 2) {
				AccountSwitch.getInstance().getAccountList().remove(accounts.get(selected).getName());
				AccountSwitch.getInstance().saveAccounts();
				this.accountListContainer.elementClicked(-1, false, 0, 0);
			}
			if(button.id == 3) {
				mc.displayGuiScreen(parent);
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.accountListContainer.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(mc.fontRendererObj, "Accounts List", this.width / 2, 20, Color.WHITE.getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void keyTyped(char keyChar, int keyCode) {
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(parent);
		}
	}
	
	class AccountSlot extends GuiSlot {

		public AccountSlot(Minecraft mc) {
			super(mc, GuiAccountsList.this.width, GuiAccountsList.this.height, 32, GuiAccountsList.this.height - 64, mc.fontRendererObj.FONT_HEIGHT + 4);
		}

		protected int getSize() {
			return GuiAccountsList.this.accounts.size();
		}

		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
			GuiAccountsList.this.selected = slotIndex;
			boolean isValidSlot = slotIndex >= 0 && slotIndex < getSize();
			if(isDoubleClick && isValidSlot) {
				GuiAccountsList.this.actionPerformed(GuiAccountsList.this.useAccountButton);
			}
			GuiAccountsList.this.deleteAccountButton.enabled = isValidSlot;
			GuiAccountsList.this.useAccountButton.enabled = isValidSlot;
		}

		protected boolean isSelected(int slotIndex) {
			return slotIndex == GuiAccountsList.this.selected;
		}

		protected void drawBackground() {
			GuiAccountsList.this.drawDefaultBackground();
		}
		
		protected int getContentHeight() {
			return getSize() * (mc.fontRendererObj.FONT_HEIGHT + 4);
		}

		protected void drawSlot(int entryID, int par2, int par3, int par4, int par5, int par6) {
			Account acct = GuiAccountsList.this.accounts.get(entryID);
			String name = acct.getName();
			GuiAccountsList.this.drawString(mc.fontRendererObj, name, par2 + 1, par3 + 1, Color.WHITE.getRGB());
		}
		
	}
}
