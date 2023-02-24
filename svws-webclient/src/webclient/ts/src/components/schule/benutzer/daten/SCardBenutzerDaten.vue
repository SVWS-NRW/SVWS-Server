<template>
	<svws-ui-content-card title="Benutzer">
		<div class="flex flex-col">
			<div class="flex gap-4">
				<svws-ui-text-input class="mb-5" v-model="name" type="text" placeholder="Name" />
				<svws-ui-text-input class="mb-5" v-model="anzeigename" type="text" placeholder="Login-Name" />
			</div>
			<!-- <svws-ui-checkbox class="mb-4 " v-model="inputIstAdmin" :disabled="manager.istInAdminGruppe()"> Admin ? </svws-ui-checkbox> -->
			<div class="flex gap-4 mt-3">
				<svws-ui-text-input class="mb-5" v-model="kennwort1" type="password" placeholder="neues Kennwort" />
				<svws-ui-text-input class="mb-5" v-model="kennwort2" type="password" placeholder="neues Kennwort wiederholen" />
			</div>
			<div>
				<svws-ui-button @click="setPassword()"> Kennwort ändern </svws-ui-button>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ref, WritableComputedRef } from "vue";

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

	function setPassword(){
		if (kennwort1.value === kennwort2.value)
			void props.setPassword(kennwort1.value)
		else
			alert("Kennwörter stimmen nicht überein")
	}

</script>
