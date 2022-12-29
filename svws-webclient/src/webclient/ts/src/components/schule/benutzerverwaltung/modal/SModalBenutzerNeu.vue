<template>
    <!-- MODAL FENSTER-->
    <svws-ui-modal  ref="modalNeuerBenutzer"  size="small" >
        
        <template #modalTitle>
		    Schüler hinzufügen
		</template>
		
        <template #modalContent>
			<div class="content-wrapper">
				<div class="input-wrapper">
					<svws-ui-text-input v-model="anzeigename" type="text" placeholder="Anzeigename" />
					<svws-ui-text-input v-model="name" type="text" placeholder="Login-Name" />
					<svws-ui-text-input v-model="passwort1" type="password" placeholder="Passwort"/>
                    <svws-ui-text-input v-model="passwort2" type="password" placeholder="Passwort"/>
                    
				</div>
			</div>	
		</template>						
		
        <template #modalActions>
			<Button	type="secondary" @click="modalNeuerBenutzer.closeModal()" >
				Abbrechen
			</Button>
			<Button  @click="createBenutzerAllgemein">
				Weiter
			</Button>
		</template>
	</svws-ui-modal>								
	
    <button class="button button--icon" @click="modalNeuerBenutzer.openModal()">
		<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
	</button>
	
    <button class="button button--icon">
		<svws-ui-icon><i-ri-file-copy-line /></svws-ui-icon>
	</button>
	
    <button class="button button--icon">
		<svws-ui-icon><i-ri-more-2-line /></svws-ui-icon>
	</button>
	
</template>

<script setup lang="ts">
    import { ref } from "vue";
    import { injectMainApp, Main } from "~/apps/Main";

    const main: Main = injectMainApp();
    const modalNeuerBenutzer = ref();
	

    const anzeigename = ref();
	const name = ref();
	const passwort1=ref();
    const passwort2=ref();
	function createBenutzerAllgemein(){
        if(passwort1.value === passwort2.value){
            main.apps.benutzer.dataBenutzer.createBenutzerAllgemein(name.value,anzeigename.value,passwort1.value);
            modalNeuerBenutzer.value.closeModal();
            anzeigename.value="";
            name.value="";
            passwort1.value="";
            passwort2.value="";
        }
        else{
            alert("Passwörter stimmen nicht überein")
        }
		
	}   
</script>

