<template>
	<div v-if="app.stammdaten.daten">
		<svws-ui-header :badge="props.id" badge-variant="light" badge-size="normal">
			<span> {{ inputTitel }} {{ inputVorname }} {{ inputNachname }} </span>
			<svws-ui-badge variant="highlight" size="normal"> {{ inputKuerzel }} </svws-ui-badge>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="RouteLehrerChildren" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
	import { RouteRecordRaw, useRouter } from "vue-router";

	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
	import { RouteLehrerChildren } from "~/router/apps/RouteLehrer";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const router = useRouter();

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag }>();

	const currentRoute: Ref<RouteRecordRaw> = ref(RouteLehrerIndividualdaten);

	const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
		get(): RouteRecordRaw {
			return currentRoute.value;
		},
		set(value: RouteRecordRaw) {
			currentRoute.value = value;
			router.push({ name: value.name, params: { id: props.id } });
		}
	});

	const inputNachname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.nachname.toString();
	});

	const inputVorname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.vorname.toString();
	});

	const inputKuerzel: ComputedRef<string | undefined> = computed(() => {
		return props.item?.kuerzel.toString();
	});

	const inputTitel: ComputedRef<string | undefined> = computed(() => {
		return props.item?.titel?.toString();
	});

</script>
