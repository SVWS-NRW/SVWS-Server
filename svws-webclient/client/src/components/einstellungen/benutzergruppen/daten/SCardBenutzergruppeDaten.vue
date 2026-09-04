<template>
	<div class="flex flex-col gap-4">
		<svws-ui-text-input class="contentFocusField" :model-value="manager().getBezeichnung()" @change="setBezeichnung" type="text" placeholder="Bezeichnung" />
		<svws-ui-checkbox type="toggle" v-model="benutzerIstAdmin" :disabled="alleKompetenzenFreigebenDisabled">Alle Kompetenzen freigeben</svws-ui-checkbox>
	</div>
</template>

<script setup lang="ts">

	import type { BenutzergruppeListeEintrag } from "@core/core/data/benutzer/BenutzergruppeListeEintrag";
	import type { BenutzergruppenManager } from "@core/core/utils/benutzer/BenutzergruppenManager";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { computed } from "vue";

	const props = defineProps<{
		manager: () => BenutzergruppenManager;
		setBezeichnung: (anzeigename: string | null) => Promise<void>;
		setIstAdmin: (istAdmin: boolean) => Promise<void>;
		mapBenutzergruppen: Map<number, BenutzergruppeListeEintrag>;
	}>();

	const benutzerState = useBenutzerState();

	const alleKompetenzenFreigebenDisabled = computed<boolean>(() => {
		const benutzerIstInAusgewaehlterGruppe = ([...benutzerState.benutzerdaten.gruppen].some(g => g.id === props.manager().daten().id));
		const benutzerIstInMehrerenAdminGruppen = [...benutzerState.benutzerdaten.gruppen].filter(e => props.mapBenutzergruppen.get(e.id)?.istAdmin === true).length > 1;
		return benutzerIstAdmin.value
			&& benutzerIstInAusgewaehlterGruppe
			&& !benutzerIstInMehrerenAdminGruppen;
	});

	const benutzerIstAdmin = computed<boolean>({
		get: () => props.manager().istAdmin(),
		set: (value: boolean) => {
			if (value !== props.manager().istAdmin()) {
				void props.setIstAdmin(value);
			}
		},
	});

</script>
