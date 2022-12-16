<template>
	<svws-ui-content-card title="Benutzergruppen">
		<div class="flex flex-row gap-4">
			<div class="-my-2 flex-none sm:-mx-6 lg:-mx-8">
				<div class="inline-block py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="border-collapse text-sm">
                            <thead class="bg-slate-100">
                             <tr>
                                <td class="border border-[#7f7f7f]/20 text-center">
                                    <svws-ui-checkbox v-model="selected"> Alle </svws-ui-checkbox>
                                </td>
                            </tr>
                        </thead>
                        <tbody>
                            <s-benutzergruppen-listeneintrag v-for="bgle in benutzergruppen" :key="bgle.id" :bgle="bgle" > </s-benutzergruppen-listeneintrag>
                        </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BenutzergruppeListeEintrag, BenutzerManager } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;


	const manager: ComputedRef<BenutzerManager | undefined> = computed(() => {
		return app.dataBenutzer.manager;
	});

    const benutzergruppen: ComputedRef<BenutzergruppeListeEintrag[] | undefined> = computed(() => {
		return main.apps.benutzergruppe.auswahl.liste;
	});

	const selected: WritableComputedRef<boolean> = computed({
        get(): boolean {
            return benutzergruppen.value?.length === manager.value?.anzahlGruppen() ?  true : false;
        },
        set(value: boolean) {
            if (value)
                app.dataBenutzer.addBenutzergruppenBenutzer(benutzergruppen.value);
            else
                app.dataBenutzer.removeBenutzergruppenBenutzer(benutzergruppen.value);
        }
    });

</script>
