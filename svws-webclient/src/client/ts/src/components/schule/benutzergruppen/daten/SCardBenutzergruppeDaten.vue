<template>
	<svws-ui-content-card title="Benutzergruppe">
		<template #actions>
			<svws-ui-checkbox v-model="inputIstAdmin"> Admin-Rechte </svws-ui-checkbox>
		</template>
		<div class="input-wrapper-1-col">
			<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import {BenutzergruppenManager} from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

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
