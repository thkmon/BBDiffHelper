package com.thkmon.diff.mng;

import com.thkmon.diff.data.LineData;
import com.thkmon.diff.data.StringList;
import com.thkmon.diff.util.LogUtil;
import com.thkmon.diff.util.StringUtil;

public class TextDiffManager {
	
	public static String NO_DIFF = "차이점 없음";
	
	
	public String diffString(String targetFilePath, String asisStr, String tobeStr) {
		
		String result = "";
		
		try {
			StringList strList1 = StringUtil.makeStringList(asisStr);
			StringList strList2 = StringUtil.makeStringList(tobeStr);
			
			result = diffStringList(targetFilePath, strList1, strList2);
			
		} catch (Exception e) {
			LogUtil.debug(e);
		}
		
		return result;
	}
	
	
	public String diffStringList(String targetFilePath, StringList asisStrList, StringList tobeStrList) {
		
		int count1 = asisStrList.size();
		int count2 = tobeStrList.size();
		int count = (count1 < count2) ? count1 : count2;
		
		// 내용 동일한 라인은 최대한 건너뛰고 시작한다.
		int i = 0;
		for (i=0; i<count; i++) {
			if (!asisStrList.get(i).equals(tobeStrList.get(i))) {
				break;
			}
		}
		
		return diffStringList(targetFilePath, asisStrList, tobeStrList, i);
	}
	
	
	public String diffStringList(String targetFilePath, StringList strList1, StringList strList2, int startRow) {
		
		StringList diffResultList = new StringList();
		
		// 테스트 코드
//		int[] arr2 = findSameLine(null, strList1, strList2, 16169, 16171);
//		if (arr2 == null) {
//			System.out.println("못찾음");
//		} else {
//			System.out.println(arr2[0] + " " + arr2[1]);
//			System.out.println(strList1.get(arr2[0]) + " " + strList2.get(arr2[1]));
//			
//		}
//		if(1==1) {
//			return;
//		}
		
		int lastIndex1 = strList1.size() - 1;
		int lastIndex2 = strList2.size() - 1;
		
		int originRow1 = startRow;
		int originRow2 = startRow;
		
		int preRow1 = startRow;
		int preRow2 = startRow;
		
		int axisRow1 = startRow;
		int axisRow2 = startRow;
		
		boolean bExitLoop = false;
		
		while (true) {
			int[] arr = findSameLine(null, strList1, strList2, axisRow1, axisRow2);
			if (arr == null) {
				preRow1 = originRow1 + 1;
				preRow2 = originRow2 + 1;
				
				axisRow1 = lastIndex1 + 1;
				axisRow2 = lastIndex2 + 1;
				
				bExitLoop = true;
				
			} else {
				preRow1 = axisRow1;
				preRow2 = axisRow2;
				
				axisRow1 = arr[0];
				axisRow2 = arr[1];
				
				originRow1 = arr[0];
				originRow2 = arr[1];
				
				if (axisRow1 == lastIndex1 && axisRow2 == lastIndex2) {
					bExitLoop = true;
				}
			}
			
			if (axisRow1 - preRow1 == 0 && axisRow2 - preRow2 == 0) {
				printModifyInfo(targetFilePath, diffResultList, strList1, strList2, preRow1, preRow2, axisRow1, axisRow2);
				
			} else if (axisRow1 - preRow1 > 0 && axisRow2 - preRow2 > 0) {
				printModifyInfo(targetFilePath, diffResultList, strList1, strList2, preRow1, preRow2, axisRow1, axisRow2);
					
			} else if (axisRow1 - preRow1 > 0) {
				printDeleteInfo(targetFilePath, diffResultList, strList1, preRow1, axisRow1);
				
			} else if (axisRow2 - preRow2 > 0) {
				printAddInfo(targetFilePath, diffResultList, strList2, preRow2, axisRow2);
			}
			
			if (!bExitLoop) {
				if (axisRow1 + 1 > lastIndex1 && axisRow2 + 1 > lastIndex2) {
					bExitLoop = true;
					continue;
				}
			}
			
			if (axisRow1 < lastIndex1) {
				axisRow1++;
			}
				
			if (axisRow2 < lastIndex2) {
				axisRow2++;
			}
			
			if (bExitLoop) {
				break;
			}
		}
		
		return printResultToConsole(diffResultList);
	}
	
	
	public String printResultToConsole(StringList diffResultList) {
		String result = "";
		
		if (diffResultList != null && diffResultList.size() > 0) {
			StringBuffer buff = new StringBuffer();
			
			String str = "";
			int diffResultCount = diffResultList.size();
			for (int i=0; i<diffResultCount; i++) {
				if (i > 0) {
					buff.append(StringUtil.ENTER);
				}
				
				str = diffResultList.get(i);
				
				if (str.indexOf("\t") > -1) {
					str = str.replace("\t", "    ");
				}
				buff.append(str);
			}
			
			result = buff.toString();
			
			if (result.startsWith(StringUtil.ENTER)) {
				result = result.substring(2);
			}
			
			result = StringUtil.ENTER + "----------" + StringUtil.ENTER + result + StringUtil.ENTER + "----------";
			
		} else {
			result = NO_DIFF;
		}
		
		return result;
	}
	
	
	public int[] findSameLine(StringList diffList, StringList strList1, StringList strList2, int rowToStart1, int rowToStart2) {
		int lastIndex1 = strList1.size() - 1;
		int lastIndex2 = strList2.size() - 1;
		
		if (rowToStart1 > lastIndex1 && rowToStart2 > lastIndex2) {
			return null;
			
		} else if (rowToStart1 > lastIndex1) {
			rowToStart1 = lastIndex1;
			
		} else if (rowToStart2 > lastIndex2) {
			rowToStart2 = lastIndex2;
		}
		
		int axisRow1 = rowToStart1;
		int axisRow2 = rowToStart2;
		
		int row1 = rowToStart1;
		int row2 = rowToStart2;
		
		boolean bLeftMode = true;
		int limitNum = 0;
		
		while (true) {
			if (bLeftMode) {
				// 내용 같으면 라인번호 리턴
				int[] tmpArr1 = getIdxArrIfSameLine(strList1, strList2, axisRow1, row2);
				if (tmpArr1 != null) {
					return tmpArr1;
				}
				
				// 내용 같으면 라인번호 리턴(보완)
				if (axisRow1 != row2) {
					int[] tmpArr2 = getIdxArrIfSameLine(strList1, strList2, axisRow1, axisRow1);
					if (tmpArr2 != null) {
						return tmpArr2;
					}
					
					int[] tmpArr3 = getIdxArrIfSameLine(strList1, strList2, row2, row2);
					if (tmpArr3 != null) {
						return tmpArr3;
					}
				}
				
			} else {
				// 내용 같으면 라인번호 리턴
				int[] tmpArr1 = getIdxArrIfSameLine(strList1, strList2, row1, axisRow2);
				if (tmpArr1 != null) {
					return tmpArr1;
				}
				
				// 내용 같으면 라인번호 리턴(보완)
				if (row1 != axisRow2) {
					int[] tmpArr2 = getIdxArrIfSameLine(strList1, strList2, row1, row1);
					if (tmpArr2 != null) {
						return tmpArr2;
					}
					
					int[] tmpArr3 = getIdxArrIfSameLine(strList1, strList2, axisRow2, axisRow2);
					if (tmpArr3 != null) {
						return tmpArr3;
					}
				}
			}
			
			// 끝까지 왔으면 다음 로우를 검사하자.
			if (row1 == lastIndex1 && row2 == lastIndex2) {
				// 진짜 끝까지 왔으면 그만 검사하자.
				if (axisRow1 == lastIndex1 && axisRow2 == lastIndex2) {
					limitNum++;
					if (limitNum > 1) {
						limitNum = 0;
						break;
					}
					
				} else {
					limitNum++;
					if (limitNum > 1) {
						limitNum = 0;
						
						if (axisRow1 + 1 <= lastIndex1) {
							axisRow1++;
							row1 = axisRow1;
						}
						if (axisRow2 + 1 <= lastIndex2) {
							axisRow2++;
							row2 = axisRow2;
						}
					}
				}
			}
			
			// 레프트 모드이면 로우2를 증가시킴
			if (bLeftMode) {
				if (row2 + 1 <= lastIndex2) {
					row2++;
				}
				bLeftMode = !bLeftMode;
			} else {
				if (row1 + 1 <= lastIndex1) {
					row1++;
				}
				bLeftMode = !bLeftMode;
			}
		}
		
		return null;
	}
	
	
	private void printModifyInfo(String targetFilePath, StringList diffResultList, StringList strList1, StringList strList2, int preRow1, int preRow2, int axisRow1, int axisRow2) {
		LineData asisData = StringUtil.getStringByList(strList1, preRow1, axisRow1 - 1);
		LineData tobeData = StringUtil.getStringByList(strList2, preRow2, axisRow2 - 1);
		String asisStr = asisData.getStr();
		String tobeStr = tobeData.getStr();
		
		boolean bAsisEmpty = false;
		if (StringUtil.checkIsSpaceLine(asisStr)) {
			bAsisEmpty = true;
		}
		
		boolean bTobeEmpty = false;
		if (StringUtil.checkIsSpaceLine(tobeStr)) {
			bTobeEmpty = true;
		}
		
		if (!bAsisEmpty && !bTobeEmpty) {
			diffResultList.add("");
			diffResultList.add("경로 : " + targetFilePath);
			diffResultList.add("라인 : " + (asisData.getBeginIdx() + 1));
			diffResultList.add("내용 : 수정");
			diffResultList.add("[AS-IS]");
			diffResultList.add(asisStr);
			
			diffResultList.add("[TO-BE]");
			diffResultList.add(tobeStr);
			
		} else if (bAsisEmpty && !bTobeEmpty) {
			diffResultList.add("");
			diffResultList.add("경로 : " + targetFilePath);
			diffResultList.add("라인 : " + (asisData.getBeginIdx() + 1));
			diffResultList.add("내용 : 추가");
			diffResultList.add(tobeStr);
			
		} else if (!bAsisEmpty && bTobeEmpty) {
			diffResultList.add("");
			diffResultList.add("경로 : " + targetFilePath);
			diffResultList.add("라인 : " + (asisData.getBeginIdx() + 1));
			diffResultList.add("내용 : 삭제");
			diffResultList.add(asisStr);
		}
	}
	
	private void printDeleteInfo(String targetFilePath, StringList diffResultList, StringList strList1, int preRow1, int axisRow1) {
		LineData delData = StringUtil.getStringByList(strList1, preRow1, axisRow1 - 1);
		String delStr = delData.getStr();
		
		boolean bEmpty = false;
		if (StringUtil.checkIsSpaceLine(delStr)) {
			bEmpty = true;
		}
		
		if (!bEmpty) {
			diffResultList.add("");
			diffResultList.add("경로 : " + targetFilePath);
			diffResultList.add("라인 : " + (delData.getBeginIdx() + 1));
			diffResultList.add("내용 : 삭제");
			diffResultList.add(delStr);
		}
	}
	
	private void printAddInfo(String targetFilePath, StringList diffResultList, StringList strList2, int preRow2, int axisRow2) {
		LineData addData = StringUtil.getStringByList(strList2, preRow2, axisRow2 - 1);
		String addStr = addData.getStr();
		
		boolean bEmpty = false;
		if (StringUtil.checkIsSpaceLine(addStr)) {
			bEmpty = true;
		}
		
		if (!bEmpty) {
			diffResultList.add("");
			diffResultList.add("경로 : " + targetFilePath);
			diffResultList.add("라인 : " + (addData.getBeginIdx() + 1));
			diffResultList.add("내용 : 추가");
			diffResultList.add(addStr);
		}
	}
	
	
	/**
	 * 내용 같으면 라인번호 리턴 메서드
	 * 
	 * strList1 리스트의 idx1 인덱스 번째 내용과
	 * strList2 리스트의 idx2 인덱스 번째 내용이 동일할 경우, 해당 인덱스를 배열에 담아 리턴한다.
	 * 
	 * @param strList1
	 * @param strList2
	 * @param idx1
	 * @param idx2
	 * @return
	 */
	private int[] getIdxArrIfSameLine(StringList strList1, StringList strList2, int idx1, int idx2) {
		String line1 = strList1.getNotError(idx1);
		String line2 = strList2.getNotError(idx2);
		if (line1 != null && line1.trim().length() > 0 && line1.equals(line2)) {
			int[] arr = new int[2];
			arr[0] = idx1;
			arr[1] = idx2;
			return arr;
		}
		
		return null;
	}
}