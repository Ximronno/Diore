package ximronno.diore.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.diore.Diore;
import ximronno.diore.api.interfaces.Account;
import ximronno.diore.api.utils.AccountUtils;
import ximronno.diore.commands.Balance;
import ximronno.diore.guis.AccountGUI;
import ximronno.diore.impl.Languages;
import ximronno.diore.model.ConfigManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InventoryListener implements Listener {

    private final Diore plugin;
    private final ConfigManager configManager;
    private final Set<String> menuTitles;

    public InventoryListener(Diore plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        menuTitles = new HashSet<>(Arrays.asList(
                configManager.getFormattedString("main-menu-title"),
                configManager.getFormattedString("account-menu-title"),
                configManager.getFormattedString("balance-menu-title"),
                configManager.getFormattedString("public-balance-menu-title"),
                configManager.getFormattedString("languages-menu-title")
        ));
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {

        String title = e.getView().getTitle();

        if(isMenuTitle(title)) {

            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();
            if(item == null) return;

            ItemMeta meta = item.getItemMeta();
            if(meta == null) return;

            Player p = (Player) e.getWhoClicked();

            PersistentDataContainer container = meta.getPersistentDataContainer();

            Account acc = plugin.getAccountManager().getAccount(p.getUniqueId()).orElse(null);
            if(acc == null) return;

            Languages language = acc.getLanguage();
            if(language == null) language = Languages.ENGLISH;

            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

            handleClick(p, container, acc, language);

        }



    }
    private boolean isMenuTitle(String title) {
        return menuTitles.contains(title);
    }
    private void handleClick(Player p, PersistentDataContainer container, Account acc, Languages language) {

        if(container.has(AccountGUI.getDioreMenuKey(), PersistentDataType.STRING)) {

            String func = container.get(AccountGUI.getDioreMenuKey(), PersistentDataType.STRING);

            if(func == null) return;
            switch(func) {

                case "back-account-menu":
                case "skull":
                    AccountGUI.openAccountMenu(p);
                    break;
                case "top":
                    break;
                case "balance":
                    AccountGUI.openBalanceMenu(p);
                    break;
                case "language":
                    AccountGUI.openLanguagesMenu(p);
                    break;
                case "public":
                    AccountGUI.openPublicBalanceMenu(p);
                    break;
                case "true":
                    AccountUtils.setPublicBalance(p, acc, language.getCFG(), true);
                    AccountGUI.openAccountMenu(p);
                    break;
                case "false":
                    AccountUtils.setPublicBalance(p, acc, language.getCFG(), false);
                    AccountGUI.openAccountMenu(p);
                    break;
                case "back-main-menu":
                    AccountGUI.openMainMenu(p);
                    break;
                default:
                    System.out.println(func);
                    break;

            }


        }
        else if(container.has(AccountGUI.getDioreLanguageKey(), PersistentDataType.STRING)) {

            String lang = container.get(AccountGUI.getDioreLanguageKey(), PersistentDataType.STRING);
            if(lang == null) return;

            Languages languageToSet;

            try {
                languageToSet = Languages.valueOf(lang);
            } catch (IllegalArgumentException e) {
                languageToSet = Languages.ENGLISH;
            }

            AccountUtils.setLanguage(p, acc, language.getCFG(), languageToSet);
            AccountGUI.openAccountMenu(p);



        }

    }

}
