package com.thkmon.diff.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SVNLogDataList extends ArrayList<SVNLogData> {

	public void addOrReplace(char newType, String newPath, long revision) {
		if (newPath == null || newPath.length() == 0) {
			return;
		}
		
		String onePath = null;
		SVNLogData oneData = null;
		int count = this.size();
		for (int i=0; i<count; i++) {
			oneData = this.get(i);
			onePath = oneData.getPath();
			
			// 동일한 패스 발견하면 끝.
			if (newPath.equals(onePath)) {
				int oldType = oneData.getType();
				
				// 동일한 패스인데 타입이 다르면 최신 정보로 갱신.
				if (oldType != newType) {
					if (oldType == 'A' && newType == 'M') {
						// 새 파일 작성 타입(A)을 유지한다.
						// 기존 리비전은 A(생성) 기준으로 되어 있는데 타입만 M(수정)으로 바꿀 경우,
						// OldRevision 에 해당하는 파일 내용이 존재하지 않아 오류가 발생한다.
						
					} else {
						oneData.setType(newType);
					}
				}
				
				// 동일한 패스일 경우 리비전을 갱신한다. 과거 리비전은 그대로 두고, 새 리비전만 바꾸면 된다.
				oneData.setNewRevision(revision);
				return;
			}
		}
		
		// 동일한 패스 발견하지 못했으면 객체 추가
		SVNLogData newData = new SVNLogData();
		newData.setPath(newPath);
		newData.setType(newType);
		
		// 바로 직전의 리비전과 비교하도록 세팅한다.
		newData.setOldRevision(revision - 1);
		newData.setNewRevision(revision);
		this.add(newData);
	}
	
	
	/**
	 * (1) SVN 내역을 우선 파일패스 순으로 정렬한다.
	 * (2) 이어서 A, D, M 순으로 정렬한 리스트를 리턴한다.
	 * 
	 * @return
	 */
	public SVNLogDataList getSortedList() {
		// (1) SVN 내역을 우선 파일패스 순으로 정렬한다.
		Collections.sort(this, new Comparator<SVNLogData>() {
			
			@Override
			public int compare(SVNLogData o1, SVNLogData o2) {
				return compareString(o1.getPath(), o2.getPath());
			}
			
			public int compareString(String s1, String s2) {
				 int result = 0;
                
                if (s1 == null) {
                	s1 = "";
                }
                
                if (s2 == null) {
                    s2 = "";
                }
                
                int len1 = s1.length();
                int len2 = s2.length();
                int len = (len1 < len2) ? len1 : len2;
                for (int i=0; i<len; i++) {
                    if (s1.charAt(i) > s2.charAt(i)) {
                        result = 1;
                        break;
                    } else if (s1.charAt(i) < s2.charAt(i)) {
                        result = -1;
                        break;
                    }
                }
                
                if (result == 0) {
                    if (len1 > len2) {
                        result = 1;
                    } else if (len1 < len2) {
                        result = -1;
                    }
                }
                
                return result;
			}
		});
		
		// (2) 이어서 A, D, M 순으로 정렬한 리스트를 리턴한다.
		SVNLogDataList newList = new SVNLogDataList();
		
		SVNLogData oneData = null;
		int count = this.size();
		
		for (int i=0; i<count; i++) {
			oneData = this.get(i);
			if (oneData.getType() == 'A') {
				newList.add(oneData);
			}
		}
		
		for (int i=0; i<count; i++) {
			oneData = this.get(i);
			if (oneData.getType() == 'D') {
				newList.add(oneData);
			}
		}
		
		for (int i=0; i<count; i++) {
			oneData = this.get(i);
			if (oneData.getType() == 'M') {
				newList.add(oneData);
			}
		}
		
		return newList;
	}
}