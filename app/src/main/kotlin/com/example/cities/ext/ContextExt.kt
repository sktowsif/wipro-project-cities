package com.example.cities.ext

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun FragmentActivity.addFragment(
    @IdRes containerViewId: Int, fragment: Fragment,
    backStack: Boolean = false
) {
    val transaction = supportFragmentManager.beginTransaction()
        .add(containerViewId, fragment)
    if (backStack) transaction.addToBackStack(null)
    transaction.commit()
}