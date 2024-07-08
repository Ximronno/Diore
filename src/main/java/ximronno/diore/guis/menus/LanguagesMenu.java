package ximronno.diore.guis.menus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ximronno.api.interfaces.Account;
import ximronno.api.interfaces.Language;
import ximronno.api.item.ItemBuilder;
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
    public void handleMenu(Player p, Account acc, Language language, PersistentDataContainer container) {

        if(container.has(key, PersistentDataType.STRING)) {

            String func = container.get(key, PersistentDataType.STRING);
            if(func == null) return;

            switch(func) {
                case "back":
                    new AccountMenu(key).open(p);
                    break;
                default:
                    Language languageToSet;

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

        Language accLanguage = acc.getLanguage();
        if(accLanguage == null) accLanguage = Languages.ENGLISH;

        for(int i = 0; i < inventory.getSize(); i++) {

            if(i == 26) inventory.setItem(i, getMenuBack(accLanguage.getConfig()));

            else inventory.setItem(i, getMenuBlank());
        }
        int languageIterator = 10;
        for(Language language : Languages.values()) {

            inventory.setItem(languageIterator, getLanguageItem(language));

            if((languageIterator + 2) % 9 == 0) languageIterator += 3;
            else languageIterator += 1;

        }
    }
    private ItemStack getLanguageItem(Language language) {
        ItemStack item = ItemBuilder.builder()
                .setMaterial(Material.PLAYER_HEAD)
                .setDisplayName(language.getName())
                .setProfileFromURL(language.getTextureURL())
                .build();

        ItemBuilder.addPersistentData(item, key, language.getRawName());

        return item;


    }
}
