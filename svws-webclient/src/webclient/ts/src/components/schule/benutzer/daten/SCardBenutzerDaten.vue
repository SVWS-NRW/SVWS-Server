<template>
	<svws-ui-content-card title="Benutzer">
		<div class="flex flex-col">
			<div class="flex gap-4">
				<svws-ui-text-input class="mb-5" v-model="name" type="text" placeholder="Name" />
				<svws-ui-text-input class="mb-5" v-model="anzeigename" type="text" placeholder="Login-Name" />
			</div>
			<svws-ui-checkbox class="mb-4 " v-model="inputIstAdmin" :disabled="manager?.istInAdminGruppe()"> Admin ? </svws-ui-checkbox>
			<div class="flex gap-4 mt-3">
				<svws-ui-text-input class="mb-5" v-model="kennwort1" type="password" placeholder="neues Kennwort" />
				<svws-ui-text-input class="mb-5" v-model="kennwort2" type="password" placeholder="neues Kennwort wiederholen" />
			</div>
			<svws-ui-button type="secondary" @click="setPassword()"> Kennwort ändern </svws-ui-button>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";

	import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";

	const props = defineProps<{
		data: DataBenutzer;
	}>();

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => props.data.manager);

	const anzeigename: WritableComputedRef<string | undefined> = computed({
		get: () => manager.value?.getAnzeigename().valueOf(),
		set: async (value) => {
			if ((value === undefined) || (value === "") || (value === manager.value?.getAnzeigename().valueOf()))
				return;
			props.data.setAnzeigename(value);
		}
	});

	const name: WritableComputedRef<string | undefined> = computed({
		get: () => manager.value?.getAnmeldename().valueOf(),
		set: async (value) => {
			if ((value === undefined) || (value === "") || (value === manager.value?.getAnmeldename().valueOf()))
				return;
			props.data.setAnmeldename(value);
		}
	});

	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get: () => manager.value?.istAdmin(),
		set: (value) => {
			if ((value === undefined) || (value === manager.value?.istAdmin()))
				return;
			props.data.setIstAdmin(value);
		}
	});

	const kennwort1 = ref();
	const kennwort2 = ref();

	function setPassword(){
		if (kennwort1.value === kennwort2.value)
			props.data.setPassword(kennwort1.value)
		else
			alert("Kennwörter stimmen nicht überein")
	}

</script>
