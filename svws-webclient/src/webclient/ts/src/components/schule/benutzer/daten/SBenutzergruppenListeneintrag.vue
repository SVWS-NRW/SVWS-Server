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
	import { computed, WritableComputedRef } from "vue";
	import { useRouter } from "vue-router";
	import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";

	const props = withDefaults(defineProps<{
		bgle: BenutzergruppeListeEintrag;
		istAlle?: boolean;
		manager: BenutzerManager;
		addBenutzerToBenutzergruppe: (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (bg_id : number) => Promise<void>;
	}>(), {
		istAlle: false
	});

	const router = useRouter();

	const selected: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.manager.IstInGruppe(props.bgle.id),
		set: (value) => {
			if (value)
				void props.addBenutzerToBenutzergruppe(props.bgle.id);
			else
				void props.removeBenutzerFromBenutzergruppe(props.bgle.id);
		}
	});

	async function doRoute() {
		await router.push({ name: routeSchuleBenutzergruppeDaten.name, params: { id: props.bgle.id } });
	}

</script>
