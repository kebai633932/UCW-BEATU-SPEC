package com.ucw.beatu.business.user.presentation.router

import androidx.fragment.app.Fragment
import com.ucw.beatu.business.user.presentation.ui.UserProfileFragment
import com.ucw.beatu.shared.router.UserProfileRouter

/**
 * UserProfileRouter 的实现
 * 将 UserProfileFragment 的创建逻辑封装为 Router 接口实现
 */
class UserProfileRouterImpl : UserProfileRouter {
    override fun createUserProfileFragment(userId: String, authorName: String, readOnly: Boolean): Fragment {
        return UserProfileFragment.newInstance(userId, readOnly)
    }
}

