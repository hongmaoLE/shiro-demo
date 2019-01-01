package cn.wolfcode.shiro.tool;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @Description TODO
 * @Date 2019/1/1 15:44
 * @Created by simple
 */
public class MD5 {
    public static void main(String[] args) {
        Md5Hash hash = new Md5Hash("666", "love", 3);
        System.out.println(hash);
    }
}
