<template>
	<tr>
		<td class="flex flex-row justify-between">
			<svws-ui-checkbox v-model="selected" :disabled="istAlle">
				<span> {{ row.id }}-{{ row.bezeichnung }}  </span>
			</svws-ui-checkbox>
			<i-ri-external-link-fill @click="goToBenutzergruppe(row.id)" class="cursor-pointer align-botton ml-3" />
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

	const props = withDefaults(defineProps<{
		row: BenutzergruppeListeEintrag;
		istAlle?: boolean;
		getBenutzerManager: () => BenutzerManager;
		addBenutzerToBenutzergruppe: (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (bg_id : number) => Promise<void>;
		goToBenutzergruppe: (b_id: number) => Promise<void>;
	}>(), {
		istAlle: false
	});

	const selected: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzerManager().IstInGruppe(props.row.id),
		set: (value) => {
			if (value)
				void props.addBenutzerToBenutzergruppe(props.row.id);
			else
				void props.removeBenutzerFromBenutzergruppe(props.row.id);
		}
	});

</script>
