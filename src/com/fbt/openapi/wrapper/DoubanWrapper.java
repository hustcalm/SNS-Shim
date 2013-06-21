package com.fbt.openapi.wrapper;

import java.io.IOException;

import com.dongxuexidu.douban4j.model.app.DoubanException;
import com.dongxuexidu.douban4j.model.shuo.DoubanShuoAttachementObj;
import com.dongxuexidu.douban4j.model.user.DoubanUserObj;
import com.dongxuexidu.douban4j.service.DoubanShuoService;
import com.dongxuexidu.douban4j.service.DoubanUserService;

public class DoubanWrapper {
	/**
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 * @throws DoubanException
	 * @throws IOException
	 */
	public DoubanUserObj getUserInfo(String accessToken, String uid) throws DoubanException, IOException {
		DoubanUserService doubanUserService = new DoubanUserService(accessToken);
		return doubanUserService.getUserProfileByUid(uid);
	}
	
	/**
	 * 
	 * @param newStatus
	 * @param att
	 * @param appKey
	 * @param accessToken
	 * @return
	 * @throws DoubanException
	 * @throws IOException
	 */
	public boolean postNewStatus (String newStatus, DoubanShuoAttachementObj att, String appKey, String accessToken) throws DoubanException, IOException {
		DoubanShuoService doubanShuoService = new DoubanShuoService(accessToken);
		return doubanShuoService.postNewStatus(newStatus, att, appKey, accessToken);
	}
}
