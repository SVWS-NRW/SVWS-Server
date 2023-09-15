<template>
	<svws-ui-content-card>
		<svws-ui-input-wrapper>
			<svws-ui-text-input :model-value="getBenutzergruppenManager().getBezeichnung()" @change="setBezeichnung" type="text" placeholder="Bezeichnung" />
			<svws-ui-checkbox type="toggle" v-model="inputIstAdmin"> Alle Kompetenzen freigeben </svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type {BenutzergruppenManager} from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		setBezeichnung : (anzeigename: string) => Promise<void>;
		setIstAdmin : (istAdmin:boolean) => Promise<void>;
	}>();

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzergruppenManager().istAdmin(),
		set: (value) => {
			if ((value === undefined) || (value === props.getBenutzergruppenManager().istAdmin()))
				return;
			void props.setIstAdmin(value);
		}
	});

</script>
