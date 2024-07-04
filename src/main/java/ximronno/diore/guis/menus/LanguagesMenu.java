package ximronno.diore.guis.menus;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.diore.guis.DioreMenu;
import ximronno.diore.impl.Languages;
import ximronno.diore.utils.AccountUtils;

public class LanguagesMenu extends DioreMenu {
    public LanguagesMenu(NamespacedKey key) {
        super(key);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return configManager.getFormattedString("languages-menu-title");
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        ItemStack item = e.getCurrentItem();
        if(item == null) return;

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;

        Player p = (Player) e.getWhoClicked();

        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);

            switch(func) {
                case "back":
                    new AccountMenu(key).open(p);
                    break;
                default:
                    Languages languageToSet;

                    try {
                        languageToSet = Languages.valueOf(func);
                    } catch (IllegalArgumentException exc) {
                        languageToSet = Languages.ENGLISH;
                    }

                    AccountUtils.setLanguage(p, acc, languageToSet);
                    new AccountMenu(key).open(p);
                    break;
            }

        }


    }

    @Override
    public void setContents(Player p) {
        Account acc = accountManager.getAccount(p.getUniqueId()).orElse(null);
        if(acc == null) return;

        Languages accLanguage = acc.getLanguage();
        if(accLanguage == null) accLanguage = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {

            if(i == 26) inventory.setItem(i, getMenuBack(accLanguage.getCFG()));

            else inventory.setItem(i, getMenuBlank());
        }
        int languageIterator = 10;
        for(Languages language : Languages.values()) {

            inventory.setItem(languageIterator, getLanguageItem(language));

            if((languageIterator + 2) % 9 == 0) languageIterator += 3;
            else languageIterator += 1;

        }
    }
    private ItemStack getLanguageItem(Languages language) {
        ItemStack item = language.getItemStack();

        ItemMeta meta = item.getItemMeta();
        if(meta == null) return null;

        meta.setDisplayName(language.getName());

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, language.name());

        item.setItemMeta(meta);

        return item;


    }
}
