package ximronno.diore.api.account;

import ximronno.diore.api.SortingVariant;

import java.util.List;

public interface AccountLeaderBoard {

    Account fromPlace(int place, SortingVariant sortVariant);

    int getPlace(Account acc, SortingVariant sortVariant);

    List<Account> getSortedLeaderBoard(SortingVariant sortVariant);

    List<Account> getLeaderBoard();

    List<Account> getOnlineLeaders();

    List<Account> getOfflineLeaders();

}
