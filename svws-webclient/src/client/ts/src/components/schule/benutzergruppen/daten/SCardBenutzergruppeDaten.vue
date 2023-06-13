<template>
	<svws-ui-content-card title="Allgemein">
		<template #actions>
			<svws-ui-toggle v-model="inputIstAdmin"> Admin-Rechte </svws-ui-toggle>
		</template>
		<svws-ui-input-wrapper>
			<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
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

	const bezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.getBenutzergruppenManager().getBezeichnung(),
		set: (value) => {
			if ((value === undefined) || (value === "") || (value === props.getBenutzergruppenManager().getBezeichnung()))
				return;
			void props.setBezeichnung(value);
		}
	});

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.getBenutzergruppenManager().istAdmin(),
		set: (value) => {
			if ((value === undefined) || (value === props.getBenutzergruppenManager().istAdmin()))
				return;
			void props.setIstAdmin(value);
		}
	});

</script>
