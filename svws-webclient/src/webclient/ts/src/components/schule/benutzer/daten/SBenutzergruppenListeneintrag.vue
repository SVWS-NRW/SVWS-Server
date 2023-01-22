<template>
	<tr>
		<td>
			<svws-ui-checkbox v-model="selected" :disabled="istAlle"> 
				<span> {{ bgle.id }}-{{ bgle.bezeichnung }} <i-ri-external-link-fill @click="doRoute()" /> </span>
			</svws-ui-checkbox>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { useRouter } from "vue-router";
	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";
	import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";

	const props = withDefaults(defineProps<{
		data: DataBenutzer;
		bgle: BenutzergruppeListeEintrag;
		istAlle?: boolean;
	}>(), {
		istAlle: false
	});

	const router = useRouter();

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => props.data.manager);

	const selected: WritableComputedRef<boolean | undefined> = computed({
		get: () => manager.value?.IstInGruppe(props.bgle.id),
		set: (value) => {
			if (value)
				props.data.addBenutzergruppeBenutzer(props.bgle.id);
			else
				props.data.removeBenutzergruppeBenutzer(props.bgle.id);
		}
	});

	function doRoute() {
		router.push({ name: routeSchuleBenutzergruppe.name, params: { id: props.bgle.id } });
	}

</script>
