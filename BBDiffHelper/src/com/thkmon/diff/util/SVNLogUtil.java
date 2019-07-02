package com.thkmon.diff.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import com.thkmon.diff.data.SVNLogData;
import com.thkmon.diff.data.SVNLogDataList;
import com.thkmon.diff.form.SVNForm;
import com.thkmon.diff.mng.TextDiffManager;

public class SVNLogUtil {
	
	private static String ENTER = StringUtil.ENTER;
	
	public static void getSVNDiff(String url, String strStartRevision, String strEndRevision) {
		
		try {
			// String svnUser = "";
			// String svnPassword = "";
			
			SVNURL svnUrl = SVNURL.parseURIDecoded(url);
			SVNRepository svnRepo = SVNRepositoryFactory.create(svnUrl);
			
			long latestRevision = svnRepo.getLatestRevision();
			long startRevision = -1;
			long endRevision = -1;
			
			if (strStartRevision != null && strStartRevision.length() > 0) {
				startRevision = StringUtil.parseLong(strStartRevision);
				if (startRevision == 0) {
					startRevision = latestRevision;
				}
			} else {
				startRevision = latestRevision;
			}
			
			if (strEndRevision != null && strEndRevision.length() > 0) {
				endRevision = StringUtil.parseLong(strEndRevision);
				if (endRevision == 0) {
					endRevision = latestRevision;
				}
			} else {
				endRevision = latestRevision;
			}
			
			SVNForm.textField2.setText("" + startRevision);
			SVNForm.textField3.setText("" + endRevision);

			// BasicAuthenticationManager authManager = new BasicAuthenticationManager(svnUser, svnPassword);
			// svnRepo.setAuthenticationManager(authManager);
			
			SVNLogDataList logDataList = getPathListFromSVNChangeLog(svnRepo, startRevision, endRevision);
			if (logDataList == null || logDataList.size() == 0) {
				return;
			}
			
			logDataList = logDataList.getSortedList();
			
			SVNLogData logData = null;
			char logType = 'M';
			String onePath = "";
			long oldRevision = 0;
			long newRevision = 0;
			
			int fileCount = logDataList.size();
			int index = 0;
			
			StringBuffer resultBuff = new StringBuffer();
			
			resultBuff.append("[패치목록]");
			
			for (int i=0; i<fileCount; i++) {
				logData = logDataList.get(i);
				
				onePath = logData.getPath();
				resultBuff.append(ENTER + onePath);
			}
			
			resultBuff.append(ENTER);
			resultBuff.append(ENTER);
			resultBuff.append("[수정내역]");
			resultBuff.append(ENTER);
			resultBuff.append("Revision : " + startRevision + " ~ " + endRevision);
			
			TextDiffManager diffMng = new TextDiffManager();
			
			for (int i=0; i<fileCount; i++) {
				index = i + 1;
				logData = logDataList.get(i);
				
				logType = logData.getType();
				onePath = logData.getPath();
				oldRevision = logData.getOldRevision();
				newRevision = logData.getNewRevision();
				
				System.out.println(index + " / " + fileCount + " 작업 시작 [" + logType + "] : " + onePath);
				
				if (logType == 'A') {
					resultBuff.append(ENTER);
					resultBuff.append("새 파일 작성함 : " + onePath);
					
				} else if (logType == 'D') {
					resultBuff.append(ENTER);
					resultBuff.append("파일 삭제함 : " + onePath);
					
				} else if (logType == 'M') {
					String asisStr = getSVNFileContent(svnRepo, oldRevision, onePath);
					String tobeStr = getSVNFileContent(svnRepo, newRevision, onePath);
					
					String result = diffMng.diffString(onePath, asisStr, tobeStr);
					if (result != null && !result.equals(TextDiffManager.NO_DIFF)) {
						resultBuff.append(result);
					}
					
				} else {
					System.out.println("알 수 없는 종류입니다. [" + logData.getType() + "]");
				}
			}
			
			System.out.println("작업 종료");
			
			File file = new File("temp.txt");
			FileWriteUtil.writeFile("temp.txt", resultBuff.toString(), false);
//			if (SVNForm.area1 != null) {
//				SVNForm.area1.setText(resultBuff.toString());
//			}
			
			// 메모장으로 실행
			ArrayList<String> list = new ArrayList<String>();
	        list.add("notepad");
	        list.add(file.getAbsolutePath());

	        ProcessBuilder pb = new ProcessBuilder(list);
	        pb.start();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static SVNLogDataList getPathListFromSVNChangeLog(SVNRepository svnRepo, long startRevision, long endRevision) throws Exception {
		if (svnRepo == null) {
			throw new Exception("svnRepo is null.");
		}
		
		if (startRevision < 0) {
			throw new Exception("startRevision(" + startRevision + ") is less than zero.");
		}
		
		if (endRevision < 0) {
			throw new Exception("endRevision(" + endRevision + ") is less than zero.");
		}
		
		long latestRevision = svnRepo.getLatestRevision();
		
		if (startRevision > latestRevision) {
			throw new Exception("startRevision(" + startRevision + ") is greater than latestRevision(" + latestRevision + ").");
		}
		
		if (endRevision > latestRevision) {
			throw new Exception("endRevision(" + endRevision + ") is greater than latestRevision(" + latestRevision + ").");
		}
		
		if (startRevision > endRevision) {
			throw new Exception("startRevision(" + startRevision + ") is greater than endRevision(" + endRevision + ").");
		}
		
		SVNLogDataList logDataList = new SVNLogDataList();
		
		Collection<SVNLogEntry> logEntries = null;
		logEntries = svnRepo.log(new String[] {""}, null, startRevision, endRevision, true, true);

		Iterator entries = logEntries.iterator();
		while (entries.hasNext()) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			if (logEntry == null) {
				continue;
			}
			
			/*
			System.out.println("---------------------------------------------");
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());
			*/
			
			if (logEntry.getChangedPaths() == null || logEntry.getChangedPaths().size() == 0) {
				continue;
			}
			
			/*
			System.out.println();
			System.out.println("changed paths:");
			*/
			
			Set changedPathsSet = logEntry.getChangedPaths().keySet();
			Iterator changedPaths = changedPathsSet.iterator();
			while (changedPaths.hasNext()) {
				SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
				if (entryPath == null) {
					continue;
				}
				
				String onePath = entryPath.getPath();
				char typeChar = entryPath.getType();
				long oneRevision = logEntry.getRevision();
				
				if (typeChar == 'A') {
					logDataList.addOrReplace(typeChar, onePath, oneRevision);
					
				} else if (typeChar == 'D') {
					logDataList.addOrReplace(typeChar, onePath, oneRevision);
					
				} else if (typeChar == 'M') {
					logDataList.addOrReplace(typeChar, onePath, oneRevision);
				}
				
				/*
				if (entryPath.getCopyPath() != null) {
					System.out.println(" " + entryPath.getType() + " " + entryPath.getPath() + "(from " + entryPath.getCopyPath() + " revision " + entryPath.getCopyRevision() + ")");
				} else {
					System.out.println(" " + entryPath.getType() + " " + entryPath.getPath());
				}
				*/
			}
		}
		
		return logDataList;
	}
	
	
//	/**
//	 * 테스트용 코드. 참고하기 위해 남겨둔다.
//	 * 
//	 * @param url
//	 * @throws Exception
//	 */
//	public static void printSVNChangeLogTest(String url) throws Exception {
//		
//		// String svnUser = "";
//		// String svnPassword = "";
//		
//		SVNURL svnUrl = SVNURL.parseURIDecoded(url);
//		SVNRepository svnRepo = SVNRepositoryFactory.create(svnUrl);
//
//		// BasicAuthenticationManager authManager = new BasicAuthenticationManager(svnUser, svnPassword);
//		// svnRepo.setAuthenticationManager(authManager);
//
//		long latestRevision = svnRepo.getLatestRevision();
//		System.out.println("latestRevision : " + latestRevision);
//
//		long startRevision = latestRevision;
//		long endRevision = latestRevision;
//
//		Collection<SVNLogEntry> logEntries = null;
//		logEntries = svnRepo.log(new String[] {""}, null, startRevision, endRevision, true, true);
//
//		Iterator entries = logEntries.iterator();
//		while (entries.hasNext()) {
//			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
//			if (logEntry == null) {
//				continue;
//			}
//			
//			System.out.println("---------------------------------------------");
//			System.out.println("revision: " + logEntry.getRevision());
//			System.out.println("author: " + logEntry.getAuthor());
//			System.out.println("date: " + logEntry.getDate());
//			System.out.println("log message: " + logEntry.getMessage());
//			
//			if (logEntry.getChangedPaths() == null || logEntry.getChangedPaths().size() == 0) {
//				continue;
//			}
//			
//			System.out.println();
//			System.out.println("changed paths:");
//			Set changedPathsSet = logEntry.getChangedPaths().keySet();
//			
//			Iterator changedPaths = changedPathsSet.iterator();
//			while (changedPaths.hasNext()) {
//				SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
//				
//				if (entryPath.getCopyPath() != null) {
//					System.out.println(" " + entryPath.getType() + " " + entryPath.getPath() + "(from " + entryPath.getCopyPath() + " revision " + entryPath.getCopyRevision() + ")");
//				} else {
//					System.out.println(" " + entryPath.getType() + " " + entryPath.getPath());
//				}
//			}
//		}
//	}
	
	
	/**
	 * SVN 특정 리비전에 해당하는 파일 내용을 파일로 저장한다.
	 * 
	 * @param svnRepo
	 * @param revision
	 * @param path
	 */
	public static void getSVNFileContentToFile(SVNRepository svnRepo, long revision, String path) {
		FileOutputStream outputStream = null;
		File file = null;
		
		try {
			file = new File("temp.txt");
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			} else {
				file.createNewFile();
			}
			
			outputStream = new FileOutputStream(file);
			// svnRepo.getFile(path, SVNRevision.HEAD.getNumber(), new SVNProperties(), outputStream);
			svnRepo.getFile(path, revision, new SVNProperties(), outputStream);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			flush(outputStream);
			close(outputStream);
		}
	}
	
	
	/**
	 * SVN 특정 리비전에 해당하는 파일 내용을 스트링으로 가져온다.
	 * 
	 * @param svnRepo
	 * @param revision
	 * @param path
	 */
	public static String getSVNFileContent(SVNRepository svnRepo, long revision, String path) throws Exception {
		String result = "";
		ByteArrayOutputStream outputStream = null;
		
		try {
			outputStream = new ByteArrayOutputStream();
			// svnRepo.getFile(path, SVNRevision.HEAD.getNumber(), new SVNProperties(), outputStream);
			svnRepo.getFile(path, revision, new SVNProperties(), outputStream);
			result = outputStream.toString();
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			flush(outputStream);
			close(outputStream);
		}
		
		return result;
	}
	
	
	/**
	 * OutputStream 객체를 비운다.
	 * 
	 * @param outputStream
	 */
	public static void flush(OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.flush();
			}
		} catch (Exception e) {
			// ignore
		}
	}
	
	
	/**
	 * OutputStream 객체를 닫는다.
	 * 
	 * @param outputStream
	 */
	public static void close(OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			// ignore
		}
	}
}