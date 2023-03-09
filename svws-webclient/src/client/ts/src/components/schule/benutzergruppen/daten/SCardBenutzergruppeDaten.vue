<template>
	<svws-ui-content-card title="Benutzergruppe">
		<div class="flex flex-col">
			<svws-ui-text-input v-model="bezeichnung" type="text" placeholder="Bezeichnung" />
			<svws-ui-checkbox v-model="inputIstAdmin"> Admin ? </svws-ui-checkbox>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzergruppenManager, BenutzerListeEintrag } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		data: BenutzergruppeDaten;
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
