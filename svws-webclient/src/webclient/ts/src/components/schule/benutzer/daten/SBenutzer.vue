<template>
	<div class="flex flex-row gap-6">
		<div class="w-2/5">
			<s-card-benutzer-daten :manager="manager"
				:set-anzeigename="setAnzeigename" :set-anmeldename="setAnmeldename"
				:set-password="setPassword" />
			<s-card-benutzer-gruppen-liste :benutzergruppen="benutzergruppen"
				:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
				:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
				:manager="manager" />
		</div>
		<div class="w-3/5">
			<s-card-benutzer-kompetenzen :manager="manager"
				:set-ist-admin="setIstAdmin" :add-kompetenz="addKompetenz" :remove-kompetenz="removeKompetenz"
				:add-benutzer-kompetenz-gruppe="addBenutzerKompetenzGruppe" :get-gruppen4-kompetenz="getGruppen4Kompetenz"
				:remove-benutzer-kompetenz-gruppe="removeBenutzerKompetenzGruppe" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerKompetenz, BenutzerKompetenzGruppe, BenutzerListeEintrag, BenutzerManager } from '@svws-nrw/svws-core-ts';
	import { ShallowRef } from 'vue';

	defineProps<{
		item: ShallowRef<BenutzerListeEintrag | undefined>;
		benutzergruppen: BenutzergruppeListeEintrag[];
		manager : BenutzerManager;
		setAnzeigename : (anzeigename : string) => Promise<void>;
		setAnmeldename : (anzeigename : string) => Promise<void>;
		setPassword : (passwort : string) => Promise<void>;
		addBenutzerToBenutzergruppe : (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (bg_id : number) => Promise<void>;
		setIstAdmin : (istAdmin: boolean) => Promise<void>;
		addKompetenz : (kompetenz : BenutzerKompetenz) => Promise<void>;
		removeKompetenz : (kompetenz : BenutzerKompetenz) => Promise<void>;
		addBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>;
		removeBenutzerKompetenzGruppe : (kompetenzgruppe : BenutzerKompetenzGruppe) => Promise<void>;
		getGruppen4Kompetenz : ( kompetenz : BenutzerKompetenz ) => string;
	}>();

</script>
