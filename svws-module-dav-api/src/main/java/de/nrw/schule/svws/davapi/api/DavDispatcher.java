package de.nrw.schule.svws.davapi.api;

import de.nrw.schule.svws.davapi.model.dav.All;
import de.nrw.schule.svws.davapi.model.dav.Bind;
import de.nrw.schule.svws.davapi.model.dav.CurrentUserPrivilegeSet;
import de.nrw.schule.svws.davapi.model.dav.Privilege;
import de.nrw.schule.svws.davapi.model.dav.Prop;
import de.nrw.schule.svws.davapi.model.dav.Propstat;
import de.nrw.schule.svws.davapi.model.dav.Read;
import de.nrw.schule.svws.davapi.model.dav.ReadAcl;
import de.nrw.schule.svws.davapi.model.dav.ReadCurrentUserPrivilegeSet;
import de.nrw.schule.svws.davapi.model.dav.Response;
import de.nrw.schule.svws.davapi.model.dav.UnBind;
import de.nrw.schule.svws.davapi.model.dav.Error;

import java.util.List;

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
	protected Error createResourceNotFoundError(String message) {
		var e = new Error();
		e.setAny(message);
		return e;
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
	protected final Response createResponse(@NotNull Prop propRequested, @NotNull Prop propResponded) {
		Response response = new Response();
		Propstat propStat200 = new Propstat();
		propStat200.setStatus(Propstat.PROP_STATUS_200_OK);
		propStat200.setProp(propResponded);
		response.getPropstat().add(propStat200);

		DynamicPropUtil dynamicPropUtil = new DynamicPropUtil(propRequested);
		Prop prop404 = dynamicPropUtil.getUnsupportedProps(propResponded);
		if (prop404 != null) {
			Propstat propStat404 = new Propstat();
			propStat404.setStatus(Propstat.PROP_STATUS_404_NOT_FOUND);
			propStat404.setProp(prop404);
			response.getPropstat().add(propStat404);
		}
		return response;
	}

    /**
     * Erzeugt ein DAV-Objekt vom Typ CurrentUserPrivilegeSet, das alle erforderlichen
     * Rechte (Privileges) für einen lesenden Zugriff auf DAV-Ressourcen enthält.
     *
     * @return CurrentUserPrivilegeSet mit Privileges für einen lesenden Zugriff.
     */
    protected final CurrentUserPrivilegeSet getReadOnlyPrivilegeSet(){
        CurrentUserPrivilegeSet privilegeSet = new CurrentUserPrivilegeSet();
        Privilege privilegeAll = new Privilege();
        privilegeAll.getContent().add(new All());
        Privilege privilegeRead = new Privilege();
        privilegeRead.getContent().add(new Read());
        Privilege privilegeReadAcl = new Privilege();
        privilegeReadAcl.getContent().add(new ReadAcl());
        Privilege privilegeBind = new Privilege();
        privilegeBind.getContent().add(new Bind());
        Privilege privilegeReadCurrentUserPrivilegeSet = new Privilege();
        privilegeReadCurrentUserPrivilegeSet.getContent().add(new ReadCurrentUserPrivilegeSet());
        Privilege privilegeUnBind = new Privilege();
        privilegeUnBind.getContent().add(new UnBind());
        privilegeSet.getPrivilege().addAll(List.of(privilegeAll, privilegeRead, privilegeReadAcl, privilegeBind,
            privilegeReadCurrentUserPrivilegeSet, privilegeUnBind));
        return privilegeSet;
    }

}
