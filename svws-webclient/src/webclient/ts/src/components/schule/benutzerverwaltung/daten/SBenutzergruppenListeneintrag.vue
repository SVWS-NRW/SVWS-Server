<template>
	<tr>
		<td>
			<svws-ui-checkbox v-model="selected" :disabled="istAlle"> 
				<span>
					{{ bgle.id }}-{{ bgle.bezeichnung }} <i-ri-external-link-fill @click="doRoute()" />
				</span>
			</svws-ui-checkbox>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { useRouter } from "vue-router";
	import { injectMainApp, Main } from "~/apps/Main";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = defineProps({
		bgle: { type: Object as () => BenutzergruppeListeEintrag, required: true },
		istAlle:{type:Boolean, default:false}
	});

	const router = useRouter();

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	});

	const selected: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined{
			return manager.value?.IstInGruppe(props.bgle.id);
		},
		set(value: boolean | undefined) {
			if (value)
				app.dataBenutzer.addBenutzergruppeBenutzer(props.bgle.id);
			else
				app.dataBenutzer.removeBenutzergruppeBenutzer(props.bgle.id);
		}
	});

	function doRoute() {
		router.push({ name: routeSchuleBenutzergruppe.name, params: { id: props.bgle.id } });
	}

</script>
