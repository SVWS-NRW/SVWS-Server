<template>
	<svws-ui-content-card title="Benutzer">
		<div class="flex flex-col">
			<svws-ui-text-input class="mb-3" v-model="name" type="text" placeholder="Name" />
			<svws-ui-text-input class="mb-3" v-model="anzeigename" type="text" placeholder="Login-Name" />
			<svws-ui-checkbox class="mb-4" v-model="inputIstAdmin" :disabled="manager?.istInAdminGruppe()"> Admin ? </svws-ui-checkbox>

			<svws-ui-text-input class="mb-3" v-model="kennwort1" type="password" placeholder="neues Kennwort" />
			<svws-ui-text-input class="mb-3" v-model="kennwort2" type="password" placeholder="neues Kennwort wiederholen" />
			<svws-ui-button type="secondary" @click="setPassword()"> Kennwort ändern </svws-ui-button>
		</div>
	</svws-ui-content-card>
	<s-card-benutzer-gruppen-liste />
</template>

<script setup lang="ts">
	import { BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	})

	const anzeigename: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return manager.value?.getAnzeigename().valueOf();
		},
		async set(val: string | undefined) {
			if ((val === undefined) || (val === "") || (val === manager.value?.getAnzeigename().valueOf()))
				return;
			app.dataBenutzer.setAnzeigename(val);
		}
	});

	const name: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return manager.value?.getAnmeldename().valueOf();
		},
		async set(val: string | undefined) {
			if ((val === undefined) || (val === "") || (val === manager.value?.getAnmeldename().valueOf()))
				return;
			app.dataBenutzer.setAnmeldename(val);
		}
	});
	const inputIstAdmin: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return manager.value?.istAdmin();
		},
		set(val) {
			if ((val === undefined) || (val === manager.value?.istAdmin()))
				return;
			app.dataBenutzer.setIstAdmin(val);
		}
	});

	const kennwort1=ref();
	const kennwort2=ref();

	function setPassword(){
		if(kennwort1.value === kennwort2.value)
			app.dataBenutzer.setPassword(kennwort1.value)
		else
			alert("Kennwörter stimmen nicht überein")
	}

</script>
