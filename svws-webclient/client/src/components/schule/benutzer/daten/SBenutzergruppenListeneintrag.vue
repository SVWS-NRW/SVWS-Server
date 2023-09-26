<template>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-checkbox type="toggle" v-model="selected" :disabled="istAlle" />
	</div>
	<div class="svws-ui-td" role="cell">
		<div class="flex items-center gap-0.5">
			<svws-ui-button type="icon" @click="gotoBenutzergruppe(row.id)">
				<i-ri-link />
			</svws-ui-button>
			{{ row.bezeichnung }}
		</div>
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-tooltip v-if="row.istAdmin">
			<i-ri-shield-star-line class="h-5 w-5 -m-0.5" />
			<template #content>Administrative Gruppe</template>
		</svws-ui-tooltip>
	</div>
</template>

<script setup lang="ts">
	import type { BenutzergruppeListeEintrag, BenutzerManager } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = withDefaults(defineProps<{
		row: BenutzergruppeListeEintrag;
		istAlle?: boolean;
		getBenutzerManager: () => BenutzerManager;
		addBenutzerToBenutzergruppe: (bg_id : number) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (bg_id : number) => Promise<void>;
		gotoBenutzergruppe: (b_id: number) => Promise<void>;
	}>(), {
		istAlle: false
	});

	const selected: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzerManager().istInGruppe(props.row.id),
		set: (value) => {
			if (value)
				void props.addBenutzerToBenutzergruppe(props.row.id);
			else
				void props.removeBenutzerFromBenutzergruppe(props.row.id);
		}
	});

</script>
