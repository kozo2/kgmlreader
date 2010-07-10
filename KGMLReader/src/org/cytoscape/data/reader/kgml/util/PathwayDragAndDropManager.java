package org.cytoscape.data.reader.kgml.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cytoscape.Cytoscape;
import cytoscape.actions.LoadNetworkTask;
import cytoscape.view.CytoscapeDesktop;

public class PathwayDragAndDropManager {

	private static final PathwayDragAndDropManager manager = new PathwayDragAndDropManager();

	public static PathwayDragAndDropManager getManager() {
		return manager;
	}

	private static final String KEGG_FTP_BASE_URL = "ftp://ftp.genome.jp/pub/kegg/xml/kgml/metabolic/";
	private static final String KEGG_PATHWAY_IMAGE_BASE_URL = "http://www.genome.jp/kegg/pathway/";

	private KEGGDropTarget target = null;

	public void activateTarget() {
		if (target == null) {
			target = new KEGGDropTarget();
			final CytoscapeDesktop desktop = Cytoscape.getDesktop();
			desktop.setDropTarget(target);
		}
	}

	// For drag and drop
	private static DataFlavor urlFlavor;

	static {
		try {
			urlFlavor = new DataFlavor(
					"application/x-java-url; class=java.net.URL");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	/**
	 * D & D
	 * 
	 * @author kono
	 * 
	 */
	private class KEGGDropTarget extends DropTarget {

		private static final long serialVersionUID = -2592819394629782471L;

		public void drop(DropTargetDropEvent dtde) {

			dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			final Transferable trans = dtde.getTransferable();
			boolean gotData = false;
			try {
				if (trans.isDataFlavorSupported(urlFlavor)) {
					URL url = (URL) trans.getTransferData(urlFlavor);
					// Add image
					gotData = true;
					System.out.println("This is valid URL: "
							+ url.toString());
					url = convertToFTP(url);
					System.out.println("NEW URL:  " + url.toString());

					LoadNetworkTask.loadURL(url, true);
				} else if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					String s = (String) trans
							.getTransferData(DataFlavor.stringFlavor);

					URL url = new URL(s);
					gotData = true;
					System.out.println("This is String.  Got DD:  "
							+ url.toString());
					url = convertToFTP(url);
					System.out.println("NEW URL:  " + url.toString());
					
					LoadNetworkTask.loadURL(url, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dtde.dropComplete(gotData);
			}
		}

		private void test(URL url) throws IOException {
			String header = null;
			InputStream is = null;

			try {
				BufferedReader br = null;

				URLConnection connection = url.openConnection();
				is = connection.getInputStream();
				try {
					br = new BufferedReader(new InputStreamReader(is));
				} finally {
					if (br != null) {
						br.close();
					}
				}
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}

		private URL convertToFTP(URL url) throws MalformedURLException {
			// If file, do nothing.
			if (url.getProtocol().contains("file")) {
				return url;
			}

			final String original = url.toString();
			final String[] parts = original.split("/");

			String ftp = original.replace(KEGG_PATHWAY_IMAGE_BASE_URL,
					KEGG_FTP_BASE_URL);
			ftp = ftp.replace("png", "xml");

			final String last = parts[parts.length - 1];
			if (last.contains("map") || last.contains("rn")) {
				ftp = ftp.replace("map", "ko");
				ftp = ftp.replace("rn", "ko");
				return new URL(ftp);
			} else if (last.contains("ko") || last.contains("ec")) {
				return new URL(ftp);
			} else {
				String org = parts[parts.length - 2];
				ftp = ftp.replace(org + "/", "organisms/" + org + "/");
				return new URL(ftp);
			}
		}
	}
}
