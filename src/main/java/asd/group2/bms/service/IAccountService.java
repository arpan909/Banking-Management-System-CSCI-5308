package asd.group2.bms.service;

import asd.group2.bms.model.account.Account;
import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.response.AccountDetailResponse;
import asd.group2.bms.payload.response.PagedResponse;
import asd.group2.bms.security.UserPrincipal;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface IAccountService {

  PagedResponse<User> getUserAccountListByStatus(AccountStatus accountStatus,
                                                 int page, int size);

  Account createAccount(User user, AccountType accountType,
                        Double balance, int creditScore) throws MessagingException, UnsupportedEncodingException;

  Account getAccountByUserId(Long userId);

  Account getAccountByAccountNumber(Long accountNumber);

  Boolean updateAccountBalance(Account account);

  AccountDetailResponse getAccountDetails(UserPrincipal currentUser);

}
