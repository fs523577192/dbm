package org.firas.dbm.dialect

/**
 * 用于获取数据库字符集的名称
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
interface DbCharset {

    fun getUTF8(): String

    fun getGB18030(): String

    fun getGBK(): String

    fun getGB2312(): String
}