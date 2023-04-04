package de.svws_nrw.davapi.api;

import de.svws_nrw.davapi.model.dav.All;
import de.svws_nrw.davapi.model.dav.CurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Error;
import de.svws_nrw.davapi.model.dav.Privilege;
import de.svws_nrw.davapi.model.dav.Prop;
import de.svws_nrw.davapi.model.dav.Propstat;
import de.svws_nrw.davapi.model.dav.Read;
import de.svws_nrw.davapi.model.dav.ReadAcl;
import de.svws_nrw.davapi.model.dav.ReadCurrentUserPrivilegeSet;
import de.svws_nrw.davapi.model.dav.Response;
import de.svws_nrw.davapi.model.dav.SimplePrivilege;
import de.svws_nrw.davapi.model.dav.Write;
import jakarta.validation.constraints.NotNull;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Verarbeiten von
 * Requests über das DAV-API des SVWSs.
 */
abstract class DavDispatcher {

	/**
	 * Erzeugt ein Error-Rückgabeobjekt mit der angegebenen Message. Dieses Objekt
	 * wird von den Dispatchern zurückgegeben, wenn die angefragten Ressource nicht
	 * gefunden wurde.
	 *
	 * @param message Fehlermeldung
	 * @return Error-Objekt
	 */
	protected Error createResourceNotFoundError(final String message) {
		// korrektes Response für Fehler sieht wie folgt aus
		//CalDav Beispiel:
//		   <D:error>
//		     <C:supported-filter>
//		       <C:prop-filter name="X-ABC-GUID"/>
//		     </C:supported-filter>
//		   </D:error>

		// WebDav Beispiel
//		<d:multistatus xmlns:d="DAV:">
//	       <d:response>
//	         <d:href>http://www.example.com/container/resource3</d:href>
//	         <d:status>HTTP/1.1 423 Locked</d:status>
//	         <d:error><d:lock-token-submitted/></d:error>
//	       </d:response>
//	     </d:multistatus>
		return new Error();

	}

	/**
	 * Erzeugt ein Response-Rückgabeobjekt. Diese wird dynamisch aufgebaut auf der
	 * Basis der im Request angefragten Properties zu einer Ressource und den
	 * tatsächlich gefundenen/ermittelten Properties. Nicht-gefundene Properties
	 * oder Ressourcen werden in einem speziellen Block (404) im Response
	 * aufgelistet.
	 *
	 * @param propRequested Angeforderte Properties zu einer Ressource
	 * @param propResponded Gefundene/Ermittelte Properties zu einer Ressource
	 * @return Response-Objekt
	 */
	protected static final Response createResponse(@NotNull final Prop propRequested, @NotNull final Prop propResponded) {
		final Response response = new Response();
		final Propstat propStat200 = new Propstat();
		propStat200.setStatus(Propstat.PROP_STATUS_200_OK);
		propStat200.setProp(propResponded);
		response.getPropstat().add(propStat200);

		final DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		final Prop prop404 = dynamicPropUtil.getUnsupportedProps(propResponded);
		if (prop404 != null) {
			final Propstat propStat404 = new Propstat();
			propStat404.setStatus(Propstat.PROP_STATUS_404_NOT_FOUND);
			propStat404.setProp(prop404);
			response.getPropstat().add(propStat404);
		}
		return response;
	}

	/**
	 * Erzeugt ein DAV-Objekt vom Typ CurrentUserPrivilegeSet, das alle
	 * erforderlichen Rechte (Privileges) für einen lesenden Zugriff auf
	 * DAV-Ressourcen enthält.
	 *
	 *
	 *
	 * @return CurrentUserPrivilegeSet mit Privileges für einen lesenden Zugriff.
	 */
	protected static final CurrentUserPrivilegeSet getReadOnlyPrivilegeSet() {
		return getPrivilegeSet(true, false);
	}

	/**
	 * Erzeugt ein DAV-Objekt vom Typ CurrentUserPrivilegeSet, das alle
	 * erforderlichen Rechte (Privileges) für einen lesenden Zugriff auf
	 * DAV-Ressourcen enthält.
	 *
	 * @param darfSchreiben ob der angemeldete Nutzer auf dem Kalender schreiben
	 *                      darf
	 * @param darfLesen     ob der angemeldete Nutzer den Kalender Lesen darf
	 *
	 * @return CurrentUserPrivilegeSet mit Privileges für einen lesenden Zugriff.
	 */
	protected static final CurrentUserPrivilegeSet getPrivilegeSet(final boolean darfLesen, final boolean darfSchreiben) {
		final CurrentUserPrivilegeSet privilegeSet = new CurrentUserPrivilegeSet();
		final Privilege privilegeAll = new Privilege();
		privilegeAll.getContent().add(new All());

		if (darfLesen) {
			addPrivilege(privilegeSet, new ReadCurrentUserPrivilegeSet());
			addPrivilege(privilegeSet, new Read());
			addPrivilege(privilegeSet, new ReadAcl());
		}
		if (darfSchreiben) {
			addPrivilege(privilegeSet, new All());
			addPrivilege(privilegeSet, new Write());
		}
		return privilegeSet;
	}

	/**
	 * Fügt einer Sammlung an Rechten ein weiteres Recht (als Instanz des Interface
	 * {@link SimplePrivilege}) hinzu
	 *
	 * @param set             die Rechtesammlung
	 * @param simplePrivilege das zuzufügende Privilege
	 */
	private static void addPrivilege(final CurrentUserPrivilegeSet set, final SimplePrivilege simplePrivilege) {
		final Privilege privilege = new Privilege();
		privilege.getContent().add(simplePrivilege);
		set.getPrivilege().add(privilege);
	}

	/**
	 * Homogenisiert eTag-Werte von unterschiedlichen Quellsystemen.
	 *
	 * In Abhängigkeit der Quellsysteme (z.B. Thunderbird, Outlook)
	 * werden teilweise Anführungszeichen als Bestandteil der Werte
	 * von eTags oder ifMatch-Headers mitgesendet oder nicht.
	 * Die DAV-Dispatcher benötigen diese Methode, um eine einheitliche
	 * Auswertung dieser Werte sicherzustellen.
	 *
	 * @param value String, kann Anführungszeichen enthalten
	 * @return String, enthält keine Anführungszeichen mehr
	 */
	protected String adjustETags(final String value) {
		return value.replace("\"", "");
	}
}
