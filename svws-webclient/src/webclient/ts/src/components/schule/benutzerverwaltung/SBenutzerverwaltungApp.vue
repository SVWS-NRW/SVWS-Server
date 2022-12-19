<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header :badge="inputId" badge-variant="light" badge-size="normal">
				<span> {{ inputBezeichnung }} </span>
				<svws-ui-badge variant="highlight" size="normal"> {{ anzeigename }} </svws-ui-badge>
			</svws-ui-header>
			<svws-ui-router-tab-bar :routes="RoutesBenutzerverwaltungChildren" v-model="selectedRoute">
				<router-view />
			</svws-ui-router-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { RouteRecordRaw, useRouter } from "vue-router";

	import { injectMainApp, Main } from "~/apps/Main";
	import { RoutesBenutzerverwaltungChildren } from "~/router/apps/RouteSchuleBenutzerverwaltung"
	import { RouteSchuleBenutzerverwaltungBenutzer } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzer";
	import { RouteSchuleBenutzerverwaltungBenutzergruppe } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzergruppe";

	const main: Main = injectMainApp();

	const router = useRouter();

	const props = defineProps<{ id?: number; item?: BenutzerListeEintrag | BenutzergruppeListeEintrag }>();

	const currentRoute: Ref<RouteRecordRaw> = ref(RouteSchuleBenutzerverwaltungBenutzer);

	const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
		get(): RouteRecordRaw {
			return props.item instanceof BenutzerListeEintrag ? RouteSchuleBenutzerverwaltungBenutzer : RouteSchuleBenutzerverwaltungBenutzergruppe;
			// return currentRoute.value;
		},
		set(value: RouteRecordRaw) {
			currentRoute.value = value;
			router.push({ name: value.name, params: { id: props.id } });
		}
	});

	const anzeigename: ComputedRef<string | false> = computed(() => {
		if (main.apps.benutzer.auswahl.ausgewaehlt)
			return main.apps.benutzer.auswahl.ausgewaehlt?.anzeigename.toString();
		if (main.apps.benutzergruppe.auswahl.ausgewaehlt)
			return main.apps.benutzergruppe.auswahl.ausgewaehlt.bezeichnung.toString();
		return false;
	});

	const inputId: ComputedRef<string | false> = computed(() => {
		if (main.apps.benutzer.auswahl.ausgewaehlt)
			return "ID: " + main.apps.benutzer.auswahl.ausgewaehlt.id;
		if (main.apps.benutzergruppe.auswahl.ausgewaehlt)
			return "ID: " + main.apps.benutzergruppe.auswahl.ausgewaehlt.id;
		return false;
	});

	const inputBezeichnung: ComputedRef<string | undefined> = computed(() => {
		if (main.apps.benutzer.auswahl.ausgewaehlt)
			return main.apps.benutzer.dataBenutzer.daten?.anzeigename?.toString();
		if (main.apps.benutzergruppe.auswahl.ausgewaehlt)
			return main.apps.benutzergruppe.dataBenutzergruppe.manager?.getBezeichnung().valueOf();
		return "";
	});

</script>
