package com.wtulich.photosupp.userhandling.logic.api.usecase;

import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;

import java.util.List;
import java.util.Optional;

public interface UcFindAccount {

    Optional<List<AccountEto>> findAllAccounts();
}
