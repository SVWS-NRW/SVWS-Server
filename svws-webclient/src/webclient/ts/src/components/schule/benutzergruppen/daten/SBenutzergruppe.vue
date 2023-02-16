<template>
	<div v-if="visible" class="flex flex-row gap-3">
		<div class="felx flex-col w-2/5">
			<s-card-benutzergruppe-daten :data="data" :set-bezeichnung="setBezeichnung" :set-ist-admin="setIstAdmin" :manager="manager"/>
			<s-benutzergruppe-add-benutzer :data="data" :benutzer="benutzer" />
		</div>

		<div class="w-3/5">
			<s-card-benutzergruppe-kompetenzen :data="data" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzergruppenManager, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps<{
		item: ShallowRef<BenutzergruppeListeEintrag | undefined>;
		data: DataBenutzergruppe;
		benutzer: BenutzerListeEintrag[];
		manager: BenutzergruppenManager;
		setBezeichnung : (anzeigename: string) => Promise<void>;
		setIstAdmin : (istAdmin:boolean) => Promise<void>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeSchuleBenutzergruppe.hidden()) && (props.item.value !== undefined);
	});

</script>
