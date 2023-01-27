<template>
	<tr>
		<td class="flex flex-row justify-between">
			<svws-ui-checkbox v-model="selected" :disabled="istAlle"> 
				<span> {{ bgle.id }}-{{ bgle.bezeichnung }}  </span>
			</svws-ui-checkbox>
			<i-ri-external-link-fill @click="doRoute()" class="cursor-pointer align-botton ml-3" />
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { useRouter } from "vue-router";
	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";
	import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";

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
				void props.data.addBenutzergruppeBenutzer(props.bgle.id);
			else
				void props.data.removeBenutzergruppeBenutzer(props.bgle.id);
		}
	});

	async function doRoute() {
		await router.push({ name: routeSchuleBenutzergruppeDaten.name, params: { id: props.bgle.id } });
	}

</script>
