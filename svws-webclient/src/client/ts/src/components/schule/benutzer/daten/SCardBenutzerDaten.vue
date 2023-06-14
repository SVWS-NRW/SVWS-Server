<template>
	<svws-ui-content-card title="Login">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input v-model="anzeigename" type="text" placeholder="Anzeigename" />
			<svws-ui-text-input v-model="name" type="text" placeholder="Name" />
			<!-- <svws-ui-checkbox class="mb-4 " v-model="inputIstAdmin" :disabled="manager.istInAdminGruppe()"> Admin ? </svws-ui-checkbox> -->
			<svws-ui-text-input v-model="kennwort1" type="password" placeholder="Neues Passwort" />
			<svws-ui-text-input v-model="kennwort2" type="password" placeholder="Neues Passwort wiederholen" />
			<div>
				<svws-ui-button :disabled="!kennwort1 || !kennwort2" @click="setPwd()"> Passwort speichern </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BenutzerManager } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed, ref } from "vue";

	const props = defineProps<{
		getBenutzerManager: () => BenutzerManager;
		setAnzeigename: (anzeigename: string) => Promise<void>;
		setAnmeldename: (anzeigename: string) => Promise<void>;
		setPassword: (passwort: string) => Promise<boolean|undefined>;
	}>();

	const anzeigename: WritableComputedRef<string | undefined> = computed({
		get: () => props.getBenutzerManager().getAnzeigename(),
		set: (value) => {
			if ((value === undefined) || (value === "") || (value === props.getBenutzerManager().getAnzeigename()))
				return;
			void props.setAnzeigename(value);
		}
	});

	const name: WritableComputedRef<string | undefined> = computed({
		get: () => props.getBenutzerManager().getAnmeldename(),
		set: (value) => {
			if ((value === undefined) || (value === "") || (value === props.getBenutzerManager().getAnmeldename()))
				return;
			void props.setAnmeldename(value);
		}
	});

	const kennwort1 = ref();
	const kennwort2 = ref();

	function setPwd(){
		if (kennwort1.value === kennwort2.value)
			void props.setPassword(kennwort1.value)
		else
			alert("Kennwörter stimmen nicht überein")
	}

</script>
