package com.wtulich.photosupp.userhandling.logic.api;

import com.wtulich.photosupp.userhandling.logic.api.usecase.UcDeleteRole;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcDeleteUser;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcFindAccount;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcFindRole;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcFindUser;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcManageRegistration;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcManageRole;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcManageUser;

public interface UserHandling extends
        UcDeleteRole,
        UcDeleteUser,
        UcFindAccount,
        UcFindRole,
        UcFindUser,
        UcManageRegistration,
        UcManageRole,
        UcManageUser {
}
